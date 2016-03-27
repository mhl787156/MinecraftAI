package org.neuroph.contrib.neat.gen.persistence.impl.serialize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.Generation;
import org.neuroph.contrib.neat.gen.Innovations;
import org.neuroph.contrib.neat.gen.persistence.PersistenceException;
import org.neuroph.contrib.neat.gen.persistence.impl.SerializationDelegate;

public class JavaSerializationDelegate implements SerializationDelegate {
	private static Logger s_log = Logger
			.getLogger(JavaSerializationDelegate.class.getName());

	public static void writeToZip(File f, Object o) throws PersistenceException {
		throw new UnsupportedOperationException("compression is not implemented yet.");
	}

	public static void writeToStream(File f, Object o)
			throws PersistenceException {
		ObjectOutputStream oos = null;

		try {
			oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(o);
		} catch (IOException e) {
			throw new PersistenceException("Could not write to file " + f.getAbsolutePath() + ".", e);
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					s_log.log(Level.WARNING,
									"IOException thrown closing ObjectOutputStream.",
									e);
				}
			}
		}
	}

	public static Object readFromZip(File f) throws PersistenceException {
		throw new UnsupportedOperationException("compression is not implemented yet.");
	}

	public static Object readFromStream(File f) throws PersistenceException {
		ObjectInputStream ois = null;

		try {
			ois = new ObjectInputStream(new FileInputStream(f));
			return ois.readObject();
		} catch (IOException e) {
			throw new PersistenceException("Could not read from file " + f.getAbsolutePath() + ".", e);
		} catch (ClassNotFoundException e) {
			throw new PersistenceException("Could not read from file " + f.getAbsolutePath() + ".", e);
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					s_log.log(Level.WARNING,
							"IOException thrown closing ObjectInputStream.", e);
				}
			}
		}
	}

	private boolean useZipCompression = true;

	public JavaSerializationDelegate() {
		this(true);
	}

	public JavaSerializationDelegate(boolean useCompression) {
		this.useZipCompression = useCompression;
	}

	public FitnessScores readFitnessScores(File f) throws PersistenceException {
		if (useZipCompression) {
			return (FitnessScores) readFromZip(f);
		}

		return (FitnessScores) readFromStream(f);
	}

	public Generation readGeneration(File f, Innovations innovations) throws PersistenceException {
		if (useZipCompression) {
			return (Generation) readFromZip(f);
		}

		return (Generation) readFromStream(f);
	}

	public Innovations readInnovations(File f) throws PersistenceException {
		if (useZipCompression) {
			return (Innovations) readFromZip(f);
		}

		return (Innovations) readFromStream(f);
	}

	public void writeFitnessScores(File f, FitnessScores o)
			throws PersistenceException {
		if (useZipCompression) {
			writeToZip(f, o);
		} else {
			writeToStream(f, o);
		}
	}

	public void writeGeneration(File f, Generation o)
			throws PersistenceException {
		if (useZipCompression) {
			writeToZip(f, o);
		} else {
			writeToStream(f, o);
		}
	}

	public void writeInnovations(File f, Innovations o)
			throws PersistenceException {
		if (useZipCompression) {
			writeToZip(f, o);
		} else {
			writeToStream(f, o);
		}
	}

	public String getFileExtension() {
		if (useZipCompression) {
			return ".bin.zip";
		}

		return ".bin";
	}

}
