package cn.nineton.onetake.util;

import java.io.*;
import java.util.*;

public class ZHConverter {
    private Properties charMap = new Properties();
    private Set conflictingSets = new HashSet();
    public static final int TRADITIONAL = 0;
    public static final int SIMPLIFIED = 1;
    private static final int NUM_OF_CONVERTERS = 2;
    private static final ZHConverter[] converters = new ZHConverter[2];
    private static final String[] propertyFiles = new String[2];

    public static ZHConverter getInstance(int converterType) {
        if (converterType >= 0 && converterType < 2) {
            if (converters[converterType] == null) {
                Class var1 = ZHConverter.class;
                synchronized(ZHConverter.class) {
                    if (converters[converterType] == null) {
                        converters[converterType] = new ZHConverter(propertyFiles[converterType]);
                    }
                }
            }

            return converters[converterType];
        } else {
            return null;
        }
    }

    public static String convert(String text, int converterType) {
        ZHConverter instance = getInstance(converterType);
        return instance.convert(text);
    }

    private ZHConverter(String propertyFile) {
        InputStream is = null;
        is = this.getClass().getClassLoader().getResourceAsStream(propertyFile);
        if (is != null) {
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(new InputStreamReader(is));
                this.charMap.load(reader);
            } catch (FileNotFoundException var15) {
                ;
            } catch (IOException var16) {
                var16.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }

                    if (is != null) {
                        is.close();
                    }
                } catch (IOException var14) {
                    ;
                }

            }
        }

        this.initializeHelper();
    }

    private void initializeHelper() {
        Map stringPossibilities = new HashMap();
        Iterator iter = this.charMap.keySet().iterator();

        while(true) {
            String key;
            do {
                if (!iter.hasNext()) {
                    iter = stringPossibilities.keySet().iterator();

                    while(iter.hasNext()) {
                        key = (String)iter.next();
                        if (((Integer)((Integer)stringPossibilities.get(key))).intValue() > 1) {
                            this.conflictingSets.add(key);
                        }
                    }

                    return;
                }

                key = (String)iter.next();
            } while(key.length() < 1);

            for(int i = 0; i < key.length(); ++i) {
                String keySubstring = key.substring(0, i + 1);
                if (stringPossibilities.containsKey(keySubstring)) {
                    Integer integer = (Integer)((Integer)stringPossibilities.get(keySubstring));
                    stringPossibilities.put(keySubstring, new Integer(integer.intValue() + 1));
                } else {
                    stringPossibilities.put(keySubstring, new Integer(1));
                }
            }
        }
    }

    public String convert(String in) {
        StringBuilder outString = new StringBuilder();
        StringBuilder stackString = new StringBuilder();

        for(int i = 0; i < in.length(); ++i) {
            char c = in.charAt(i);
            String key = "" + c;
            stackString.append(key);
            if (!this.conflictingSets.contains(stackString.toString())) {
                if (this.charMap.containsKey(stackString.toString())) {
                    outString.append(this.charMap.get(stackString.toString()));
                    stackString.setLength(0);
                } else {
                    CharSequence sequence = stackString.subSequence(0, stackString.length() - 1);
                    stackString.delete(0, stackString.length() - 1);
                    this.flushStack(outString, new StringBuilder(sequence));
                }
            }
        }

        this.flushStack(outString, stackString);
        return outString.toString();
    }

    private void flushStack(StringBuilder outString, StringBuilder stackString) {
        while(stackString.length() > 0) {
            if (this.charMap.containsKey(stackString.toString())) {
                outString.append(this.charMap.get(stackString.toString()));
                stackString.setLength(0);
            } else {
                outString.append("" + stackString.charAt(0));
                stackString.delete(0, 1);
            }
        }

    }

    String parseOneChar(String c) {
        return this.charMap.containsKey(c) ? (String)this.charMap.get(c) : c;
    }

    static {
        propertyFiles[0] = "zh2Hant.properties";
        propertyFiles[1] = "zh2Hans.properties";
    }
}