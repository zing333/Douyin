package cn.nineton.onetake.util;

public class BinTools {
    public static final String hex = "0123456789ABCDEF";

    public BinTools() {
        super();
    }

    public static String bin2hex(byte[] arg5) {
        String v0;
        if (arg5 == null) {
            v0 = "";
        } else {
            StringBuffer v1 = new StringBuffer(arg5.length * 2);
            int v0_1;
            for (v0_1 = 0; v0_1 < arg5.length; ++v0_1) {
                int v2 = (arg5[v0_1] + 256) % 256;
                v1.append("0123456789ABCDEF".charAt(v2 / 16 & 15));
                v1.append("0123456789ABCDEF".charAt(v2 % 16 & 15));
            }

            v0 = v1.toString();
        }

        return v0;
    }

    public static int hex2bin(char arg3) {
        int v0 = 0;
        if (arg3 < 48 || arg3 > 57) {
            if (arg3 >= 65 && arg3 <= 70) {
                return arg3 - 55;
            }
            if (arg3 >= 97 && arg3 <= 102) {
                return arg3 - 87;
            }
        } else {
            v0 = arg3 - 48;
        }
        return v0;
    }

    public static byte[] hex2bin(String arg5) {
        int v0 = 0;
        if (arg5 == null) {
            arg5 = "";
        } else if (arg5.length() % 2 != 0) {
            arg5 = new StringBuffer().append("0").append(arg5).toString();
        }

        byte[] v2 = new byte[arg5.length() / 2];
        int v1 = 0;
        while (v1 < arg5.length()) {
            int v3 = v1 + 1;
            char v4 = arg5.charAt(v1);
            v1 = v3 + 1;
            v2[v0] = ((byte) (BinTools.hex2bin(arg5.charAt(v3)) + BinTools.hex2bin(v4) * 16));
            ++v0;
        }

        return v2;
    }

    public static void main(String[] arg5) {
        int v4 = 256;
        int v0 = 0;
        byte[] v3 = new byte[v4];
        byte v1;
        for (v1 = 0; v0 < v4; v1 = ((byte) (v1 + 1))) {
            v3[v0] = v1;
            ++v0;
        }

        String v0_1 = BinTools.bin2hex(v3);
        if (!v0_1.equals(BinTools.bin2hex(BinTools.hex2bin(v0_1)))) {
            throw new AssertionError("Mismatch");
        }
    }
}