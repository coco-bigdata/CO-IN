package com.example;

import java.io.*;

public class ReadTest {
    public static void write(final String data, final File file) throws IOException {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(data.getBytes());
            out.flush();
        }
        finally {
            close(out);
        }
    }

    public static String read(final InputStream in) throws IOException {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            final byte[] buf = new byte[1024];
            int len = -1;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
            final byte[] data = out.toByteArray();
            return new String(data);
        }
        finally {
            close(in);
            close(out);
        }
    }

    public static void close(final Closeable c) {
        if (c != null) {
            try {
                c.close();
            }
            catch (IOException ex) {}
        }
    }
}
