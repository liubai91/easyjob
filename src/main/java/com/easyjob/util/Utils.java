package com.easyjob.util;

import java.io.*;

public class Utils {

    public static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static byte[] readByteArrayFromResource(String resource) throws IOException {
        if (resource == null
                || resource.isEmpty()
                || resource.contains("..")
                || resource.contains("?")
                || resource.contains(":")) {
            return null;
        }

        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            if (in == null) {
                return null;
            }

            return readByteArray(in);
        } finally {
            close(in);

        }
    }



    public static String readFromResource(String resource) throws IOException {
        if (resource == null
                || resource.isEmpty()
                || resource.contains("..")
                || resource.contains("?")
                || resource.contains(":")) {
            return null;
        }

        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            if (in == null) {
                in = Utils.class.getResourceAsStream(resource);
            }

            if (in == null) {
                return null;
            }

            String text = Utils.read(in);
            return text;
        } finally {
            Utils.close(in);
        }
    }

    public static String read(InputStream in) {
        if (in == null) {
            return null;
        }

        InputStreamReader reader;
        try {
            reader = new InputStreamReader(in, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return read(reader);
    }

    public static String read(Reader reader) {
        if (reader == null) {
            return null;
        }

        try {
            StringWriter writer = new StringWriter();

            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }

            return writer.toString();
        } catch (IOException ex) {
            throw new IllegalStateException("read error", ex);
        }
    }

    public static byte[] readByteArray(InputStream input) throws IOException {
        if (input == null) {
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    public static long copy(InputStream input, OutputStream output) throws IOException {
        final int EOF = -1;

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    private static void close(InputStream in) {
        try {
            if (in !=null) {
                in.close();
            }
        } catch (Exception e) {

        }
    }
}
