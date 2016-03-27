package org.neuroph.contrib.neat.gen.persistence.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.Generation;
import org.neuroph.contrib.neat.gen.Innovations;
import org.neuroph.contrib.neat.gen.persistence.Persistence;
import org.neuroph.contrib.neat.gen.persistence.PersistenceException;

/**
 * An implementation of the <code>Persistence</code> interface that will only
 * persist the changes between successive generations. This is to preserve disk
 * space, as a large population can rapidly generate a lot of data.
 * 
 * The actual writing of the data to disk is passed off to a
 * <code>SerializationDelegate</code> to perform, this class is just responsible
 * for managing the saving and loading of the deltas.
 * 
 * @author Aidan Morgan
 */
public class DirectoryOutputPersistence implements Persistence {
	/**
	 * The prefix for files that store generations.
	 */
	public static final String GENERATION_PREFIX = "generation-";

	/**
	 * The prefix for files that store innovations.
	 */
	public static final String INNOVATION_PREFIX = "innovation-";

	/**
	 * The prefix for files that store fitness.
	 */
	public static final String FITNESS_PREFIX = "fitness-";

	/**
	 * The default directory for storing files in.
	 */
	public static final String DEFAULT_BASE_DIRECTORY = "output";

	/**
	 * The <code>baseDirectory</code> to store all of the files in.
	 */
	private File baseDirectory = null;

	/**
	 * The <code>Innovations</code> that was last persisted (i.e. the last
	 * <code>Innovations</code> that was written to disk.
	 */
	private Innovations persistedInnovations;

	/**
	 * The <code>FitnessScores</code> that was last persisted (i.e. the last
	 * <code>FitnessScores</code> that was written to disk.
	 */
	private FitnessScores persistedFitnessScores;

	/**
	 * The <code>SerializationDelegate</code> that performs the physical file
	 * writing.
	 */
	private SerializationDelegate delegate;

	/**
	 * Constructor.
	 * 
	 * @param delegate
	 *            the <code>SerializationDelegate</code> that performs the
	 *            physical file writing.
	 */
	public DirectoryOutputPersistence(SerializationDelegate delegate) {
		this(DEFAULT_BASE_DIRECTORY, delegate);
	}

	public DirectoryOutputPersistence(String baseDirectory,
			SerializationDelegate delegate) {
		if (baseDirectory == null) {
			baseDirectory = ".";
		}

		if (delegate == null) {
			throw new IllegalArgumentException(
					"SerializationDelegate cannot be null.");
		}

		this.delegate = delegate;
		setBaseDirectory(baseDirectory);
	}

	public File getBaseDirectory() {
		return baseDirectory;
	}

	public void setBaseDirectory(String dir) {
		File temp = new File(dir);

		if (!temp.exists()) {
			if (!temp.mkdirs()) {
				throw new IllegalArgumentException(
						"Could not create base directory " + dir + ".");
			}
		}

		if (!temp.canRead() || !temp.canWrite()) {
			throw new IllegalStateException("Cannot read/write directory "
					+ dir + ".");
		}

		baseDirectory = temp;
	}
	
	@Override
	public long getGenerationCount() {
		return getFilesForType(GENERATION_PREFIX).length;
	}

	public void addGeneration(Innovations i, Generation g, FitnessScores fitness)
			throws PersistenceException {
		// if we have existing entries, then calculate the delta and only store
		// that.
		FitnessScores scores = fitness;
		if (persistedFitnessScores != null) {
			scores = FitnessScores
					.computeAdded(persistedFitnessScores, fitness);
		}
		saveFitnessScores(g.getGenerationNumber(), scores);

		// if we have existing entries, then calculate the delta and only store
		// that.
		Innovations innovations = i;
		if (persistedInnovations != null) {
			innovations = Innovations.calculateAdded(persistedInnovations, i);
		}
		saveInnovations(g.getGenerationNumber(), innovations);

		// a generation has no concept of a delta, they are all unique, so just
		// save.
		saveGeneration(g.getGenerationNumber(), g);
	}

	public FitnessScores loadFitnessScores() throws PersistenceException {
		return loadFitnessScores(getGenerationCount());
	}
	
	public FitnessScores loadFitnessScores(long generation) throws PersistenceException {
		if (persistedFitnessScores != null) {
			return persistedFitnessScores;
		}

		FitnessScores scores = null;
		File[] files = getFilesForType(FITNESS_PREFIX);

		if (files.length == 0) {
			return scores;
		}

		for(int i = 1; i <= generation; i++) {
			File f = new File(baseDirectory, FITNESS_PREFIX + i + delegate.getFileExtension());
			
			if(!f.exists()) {
				throw new IllegalArgumentException("Cannot load fitness from file " + f.getAbsolutePath() + ".");
			}
			
			FitnessScores score = (FitnessScores) delegate.readFitnessScores(f);

			if (scores == null) {
				scores = score;
			} else {
				FitnessScores.append(scores, score);
			}
		}

		// update the base
		persistedFitnessScores = scores;

		return scores;
	}

	public Innovations loadInnovations() throws PersistenceException {
		return loadInnovations(getGenerationCount());
	}
	
	public Innovations loadInnovations(long generation) throws PersistenceException {
		if (persistedInnovations != null) {
			return persistedInnovations;
		}

		Innovations scores = null;
		File[] files = getFilesForType(INNOVATION_PREFIX);

		if (files.length == 0) {
			return scores;
		}

		for(int i = 1; i <= generation; i++) {
			File f = new File(baseDirectory, INNOVATION_PREFIX + i + delegate.getFileExtension());
			
			if(!f.exists()) {
				throw new IllegalArgumentException("Cannot load innovations from file " + f.getAbsolutePath() + ".");
			}
			
			Innovations read = (Innovations) delegate.readInnovations(f);

			if (scores == null) {
				scores = read;
			} else {
				Innovations.append(scores, read);
			}
		}

		persistedInnovations = scores;

		return scores;
	}

	public Generation loadGeneration(Innovations innovations) throws PersistenceException {
		return loadGeneration(getGenerationCount(), innovations);
	}
	

	@Override
	public Generation loadGeneration(long l, Innovations innovations) throws PersistenceException {
		Generation gen = null;

		File[] files = getFilesForType(GENERATION_PREFIX);

		// there's no files, so return null
		if (files.length == 0) {
			return gen;
		}

		File generation = null;
		String targetName = GENERATION_PREFIX + l + delegate.getFileExtension();
		for(File f : files) {
			if(f.getName().equals(targetName)) {
				generation = f;
			}
		}

		if(generation == null) {
			throw new PersistenceException("Cannot find Generation file " + targetName + ".");
		}
		
		return (Generation) delegate.readGeneration(generation, innovations);
	}	

	private void saveFitnessScores(long generation, FitnessScores scores)
			throws PersistenceException {
		File outputFile = getFileForFitnessScores(generation);
		delegate.writeFitnessScores(outputFile, scores);
	}

	private void saveInnovations(long generation, Innovations innovations)
			throws PersistenceException {
		File outputFile = getFileForInnovations(generation);
		delegate.writeInnovations(outputFile, innovations);
	}

	private void saveGeneration(long generation, Generation g)
			throws PersistenceException {
		File outputFile = getFileForGeneration(generation);
		delegate.writeGeneration(outputFile, g);
	}

	private File getFileForGeneration(long generation) {
		return new File(baseDirectory, GENERATION_PREFIX + generation
				+ delegate.getFileExtension());
	}

	private File getFileForInnovations(long generation) {
		return new File(baseDirectory, INNOVATION_PREFIX + generation
				+ delegate.getFileExtension());
	}

	private File getFileForFitnessScores(long generation) {
		return new File(baseDirectory, FITNESS_PREFIX + generation
				+ delegate.getFileExtension());
	}

	private File[] getFilesForType(String prefix) {
		File[] files = baseDirectory.listFiles(new PrefixSuffixFilenameFilter(
				prefix, delegate.getFileExtension()));
		Arrays.sort(files, new PrefixSuffixFilenameComparator(prefix, delegate
				.getFileExtension()));

		return files;
	}
	
	public static class PrefixSuffixFilenameFilter implements FileFilter {
		private String prefix;
		private String suffix;

		public PrefixSuffixFilenameFilter(String prefix, String suffix) {
			super();
			this.prefix = prefix;
			this.suffix = suffix;
		}

		public boolean accept(File pathname) {
			String name = pathname.getName();
			return name.startsWith(prefix) && name.endsWith(suffix);
		}
	}

	public static class PrefixSuffixFilenameComparator implements
			Comparator<File> {
		private String prefix;
		private String suffix;

		public PrefixSuffixFilenameComparator(String prefix, String suffix) {
			super();
			this.prefix = prefix;
			this.suffix = suffix;
		}

		public int compare(File o1, File o2) {
			String nameOne = o1.getName();
			String nameTwo = o2.getName();

			Long one = Long.valueOf(nameOne.substring(nameOne.indexOf(prefix)
					+ prefix.length(), nameOne.indexOf(suffix)));
			Long two = Long.valueOf(nameTwo.substring(nameTwo.indexOf(prefix)
					+ prefix.length(), nameTwo.indexOf(suffix)));

			return one.compareTo(two);
		}

	}
}
