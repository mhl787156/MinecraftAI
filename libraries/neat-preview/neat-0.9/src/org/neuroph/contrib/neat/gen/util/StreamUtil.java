package org.neuroph.contrib.neat.gen.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class for streams, allows users to copy from an inputstream into an
 * outputstream.
 * 
 * @author Aidan Morgan
 */
public class StreamUtil {

	/**
	 * The size of the buffer.
	 */
	private static final int BUFFER_SIZE = 1024;

	/**
	 * Default constructor.
	 */
	private StreamUtil() {
		super();
	}

	/**
	 * Copy file from an inputstream to an outputstream.
	 * 
	 * @param in
	 *            does not close.
	 * @param out
	 *            closes after copy if automaticallyCloseOutputStream is
	 *            <code>true</code>.
	 * @throws java.io.IOException
	 *             thrown if there is a writing error to the file.
	 */
	public static void copyFile(InputStream in, OutputStream out,
			boolean automaticallyCloseOutputStream) throws IOException {
		assert in != null : "Input stream is null. This should not occur";
		assert out != null : "Output stream is null. This should not occur";

		byte[] buffer = new byte[BUFFER_SIZE];
		int len;
		try {
			while ((len = in.read(buffer)) >= 0) {
				// System.out.println("Read " + len + " bytes from stream.");
				out.write(buffer, 0, len);
			}
		} finally {
			if (automaticallyCloseOutputStream) {
				out.close();
			}
		}
	}
}