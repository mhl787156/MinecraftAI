package org.neuroph.contrib.neat.gen.persistence.impl.xstream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.Generation;
import org.neuroph.contrib.neat.gen.Innovations;
import org.neuroph.contrib.neat.gen.persistence.PersistenceException;
import org.neuroph.contrib.neat.gen.persistence.impl.SerializationDelegate;

import com.thoughtworks.xstream.XStream;

public class XStreamSerializationDelegate implements SerializationDelegate {
	private static final boolean DEFAULT_USE_COMPRESSION = true;

	private static void writeToStream(File f, Serializable o)
			throws PersistenceException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			XStream xs = new XStream();
			xs.toXML(o, fos);
		} catch (IOException e) {
			throw new PersistenceException("Could not write "
					+ o.getClass().getName() + " to " + f.getAbsolutePath()
					+ ".", e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// log and ignore
				}
			}
		}
	}

	private static void writeToZip(File f, Serializable o)
			throws PersistenceException {
		ZipOutputStream zos = null;
		FileOutputStream oos = null;

		try {
			zos = new ZipOutputStream(new FileOutputStream(f));
			ZipEntry entry = new ZipEntry("entry");
			zos.putNextEntry(entry);

			XStream xs = new XStream();
			xs.toXML(o, zos);
			zos.closeEntry();
		} catch (IOException e) {
			throw new PersistenceException("Could not write "
					+ o.getClass().getName() + " to " + f.getAbsolutePath()
					+ ".", e);
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					// log and ignore
				}
			}

			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					// log and ignore.
				}

			}

		}
	}

	private static Serializable readFromStream(File f)
			throws PersistenceException {
		FileInputStream ois = null;

		try {
			ois = new FileInputStream(f);

			XStream xs = new XStream();
			return (Serializable) xs.fromXML(ois);
		} catch (IOException e) {
			throw new PersistenceException("Could not read object from "
					+ f.getAbsolutePath() + ".", e);
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					// log and ignore.
				}
			}
		}

	}

	private static Serializable readFromZip(File f) throws PersistenceException {
		ZipInputStream zis = null;
		FileInputStream ois = null;

		try {
			zis = new ZipInputStream(new FileInputStream(f));
			zis.getNextEntry();

			XStream xs = new XStream();
			return (Serializable) xs.fromXML(zis);
		} catch (IOException e) {
			throw new PersistenceException(
					"Could not read compressed object from "
							+ f.getAbsolutePath() + ".", e);
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					// log and ignore.
				}
			}

			if (zis != null) {
				try {
					zis.close();
				} catch (IOException e) {
					// log and ignore
				}
			}
		}
	}

	private boolean useZipCompression = DEFAULT_USE_COMPRESSION;

	public XStreamSerializationDelegate() {
		this(DEFAULT_USE_COMPRESSION);
	}

	public XStreamSerializationDelegate(boolean useCompression) {
		this.useZipCompression = useCompression;
	}

	public String getFileExtension() {
		if (useZipCompression) {
			return ".xml.zip";
		}

		return ".xml";
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

	public void writeFitnessScores(File outFile, FitnessScores s)
			throws PersistenceException {
		if (useZipCompression) {
			writeToZip(outFile, s);
		} else {
			writeToStream(outFile, s);
		}
	}

	public void writeGeneration(File outFile, Generation s)
			throws PersistenceException {
		if (useZipCompression) {
			writeToZip(outFile, s);
		} else {
			writeToStream(outFile, s);
		}
	}

	public void writeInnovations(File outFile, Innovations s)
			throws PersistenceException {
		if (useZipCompression) {
			writeToZip(outFile, s);
		} else {
			writeToStream(outFile, s);
		}
	}

}
