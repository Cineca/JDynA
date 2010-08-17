/*
 * JDynA, Dynamic Metadata Management for Java Domain Object
 *
 * Copyright (c) 2008, CILEA and third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by CILEA.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License v3 or any later version, as published 
 * by the Free Software Foundation, Inc. <http://fsf.org/>.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package it.cilea.osd.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Classe che contiene metodi statici di utilità per gli altri oggetti;
 * 
 * @author pascarelli
 *
 */
public class Utils {
	  /**
	   * Compare two objects according to Collection semantics.
	   *
	   * @param o1 the first object
	   * @param o2 the second object
	   * @return o1 == o2 || (o1 != null && o1.equals(o2))
	   */
	  public static final boolean equals(Object o1, Object o2)
	  {
	    return o1 == o2 || (o1 != null && o1.equals(o2));
	  }
	  
		public static void bufferedCopy(final InputStream source,
				final OutputStream destination) throws IOException {
			final BufferedInputStream input = new BufferedInputStream(source);
			final BufferedOutputStream output = new BufferedOutputStream(
					destination);
			copy(input, output);
			output.flush();
		}

		public static void copy(final InputStream input, final OutputStream output)
				throws IOException {
			final int BUFFER_SIZE = 1024 * 4;
			final byte[] buffer = new byte[BUFFER_SIZE];

			while (true) {
				final int count = input.read(buffer, 0, BUFFER_SIZE);

				if (-1 == count) {
					break;
				}

				// write out those same bytes
				output.write(buffer, 0, count);
			}

			// needed to flush cache
			// output.flush();
		}

}
