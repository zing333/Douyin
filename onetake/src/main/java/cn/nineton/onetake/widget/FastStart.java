package cn.nineton.onetake.widget;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

public class FastStart {
    private static final int ATOM_PREAMBLE_SIZE = 8;
    private static final int CMOV_ATOM = fourCcToInt(new byte[]{(byte) 99, (byte) 109, (byte) 111, (byte) 118});
    private static final int CO64_ATOM = fourCcToInt(new byte[]{(byte) 99, (byte) 111, (byte) 54, (byte) 52});
    private static final int FREE_ATOM = fourCcToInt(new byte[]{(byte) 102, (byte) 114, (byte) 101, (byte) 101});
    private static final int FTYP_ATOM = fourCcToInt(new byte[]{(byte) 102, (byte) 116, (byte) 121, (byte) 112});
    private static final int JUNK_ATOM = fourCcToInt(new byte[]{(byte) 106, (byte) 117, (byte) 110, (byte) 107});
    private static final int MDAT_ATOM = fourCcToInt(new byte[]{(byte) 109, (byte) 100, (byte) 97, (byte) 116});
    private static final int MOOV_ATOM = fourCcToInt(new byte[]{(byte) 109, (byte) 111, (byte) 111, (byte) 118});
    private static final int PICT_ATOM = fourCcToInt(new byte[]{(byte) 80, (byte) 73, (byte) 67, (byte) 84});
    private static final int PNOT_ATOM = fourCcToInt(new byte[]{(byte) 112, (byte) 110, (byte) 111, (byte) 116});
    private static final int SKIP_ATOM = fourCcToInt(new byte[]{(byte) 115, (byte) 107, (byte) 105, (byte) 112});
    private static final int STCO_ATOM = fourCcToInt(new byte[]{(byte) 115, (byte) 116, (byte) 99, (byte) 111});
    private static final int UUID_ATOM = fourCcToInt(new byte[]{(byte) 117, (byte) 117, (byte) 105, (byte) 100});
    private static final int WIDE_ATOM = fourCcToInt(new byte[]{(byte) 119, (byte) 105, (byte) 100, (byte) 101});
    public static boolean sDEBUG = false;

    public static class QtFastStartException extends Exception {
        private QtFastStartException(String detailMessage) {
            super(detailMessage);
        }
    }

    public static class MalformedFileException extends QtFastStartException {
        private MalformedFileException(String detailMessage) {
            super(detailMessage);
        }
    }

    public static class UnsupportedFileException extends QtFastStartException {
        private UnsupportedFileException(String detailMessage) {
            super(detailMessage);
        }
    }

    public static void main(String[] args) {
        sDEBUG = true;
        if (args.length < 2) {
            printf("input file and output file is required.", new Object[0]);
            System.exit(1);
        }
        try {
            fastStart(new File(args[0]), new File(args[1]));
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                printe(e, "Failed to close file: ", new Object[0]);
            }
        }
    }

    static long uint32ToLong(int int32) {
        return ((long) int32) & 4294967295L;
    }

    static int uint32ToInt(int uint32) throws UnsupportedFileException {
        if (uint32 >= 0) {
            return uint32;
        }
        throw new UnsupportedFileException("uint32 value is too large");
    }

    static int uint32ToInt(long uint32) throws UnsupportedFileException {
        if (uint32 <= 2147483647L && uint32 >= 0) {
            return (int) uint32;
        }
        throw new UnsupportedFileException("uint32 value is too large");
    }

    static long uint64ToLong(long uint64) throws UnsupportedFileException {
        if (uint64 >= 0) {
            return uint64;
        }
        throw new UnsupportedFileException("uint64 value is too large");
    }

    private static int fourCcToInt(byte[] byteArray) {
        return ByteBuffer.wrap(byteArray).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    private static void printf(String format, Object... args) {
        if (sDEBUG) {
            System.err.println("QtFastStart: " + String.format(format, args));
        }
    }

    private static void printe(Throwable e, String format, Object... args) {
        printf(format, args);
        if (sDEBUG) {
            e.printStackTrace();
        }
    }

    private static boolean readAndFill(FileChannel infile, ByteBuffer buffer) throws IOException {
        buffer.clear();
        int size = infile.read(buffer);
        buffer.flip();
        return size == buffer.capacity();
    }

    private static boolean readAndFill(FileChannel infile, ByteBuffer buffer, long position) throws IOException {
        buffer.clear();
        int size = infile.read(buffer, position);
        buffer.flip();
        return size == buffer.capacity();
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x002e  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x002e  */
    public static boolean fastStart(java.io.File r8, java.io.File r9) throws java.io.IOException, FastStart.MalformedFileException, FastStart.UnsupportedFileException {
        /*
        r6 = 0;
        r0 = 0;
        r3 = 0;
        r1 = new java.io.FileInputStream;	 Catch:{ all -> 0x0025 }
        r1.<init>(r8);	 Catch:{ all -> 0x0025 }
        r2 = r1.getChannel();	 Catch:{ all -> 0x0032 }
        r4 = new java.io.FileOutputStream;	 Catch:{ all -> 0x0032 }
        r4.<init>(r9);	 Catch:{ all -> 0x0032 }
        r5 = r4.getChannel();	 Catch:{ all -> 0x0035 }
        r6 = fastStartImpl(r2, r5);	 Catch:{ all -> 0x0035 }
        safeClose(r1);
        safeClose(r4);
        if (r6 != 0) goto L_0x0024;
    L_0x0021:
        r9.delete();
    L_0x0024:
        return r6;
    L_0x0025:
        r7 = move-exception;
    L_0x0026:
        safeClose(r0);
        safeClose(r3);
        if (r6 != 0) goto L_0x0031;
    L_0x002e:
        r9.delete();
    L_0x0031:
        throw r7;
    L_0x0032:
        r7 = move-exception;
        r0 = r1;
        goto L_0x0026;
    L_0x0035:
        r7 = move-exception;
        r3 = r4;
        r0 = r1;
        goto L_0x0026;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.VideoTools.FastStart.fastStart(java.io.File, java.io.File):boolean");
    }

    private static boolean fastStartImpl(FileChannel infile, FileChannel outfile) throws IOException, MalformedFileException, UnsupportedFileException {
        ByteBuffer atomBytes = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
        int atomType = 0;
        long atomSize = 0;
        ByteBuffer ftypAtom = null;
        long startOffset = 0;
        while (readAndFill(infile, atomBytes)) {
            atomSize = uint32ToLong(atomBytes.getInt());
            atomType = atomBytes.getInt();
            if (atomType == FTYP_ATOM) {
                int ftypAtomSize = uint32ToInt(atomSize);
                ftypAtom = ByteBuffer.allocate(ftypAtomSize).order(ByteOrder.BIG_ENDIAN);
                atomBytes.rewind();
                ftypAtom.put(atomBytes);
                if (infile.read(ftypAtom) < ftypAtomSize - 8) {
                    break;
                }
                ftypAtom.flip();
                startOffset = infile.position();
            } else if (atomSize == 1) {
                atomBytes.clear();
                if (!readAndFill(infile, atomBytes)) {
                    break;
                }
                atomSize = uint64ToLong(atomBytes.getLong());
                infile.position((infile.position() + atomSize) - 16);
            } else {
                infile.position((infile.position() + atomSize) - 8);
            }
            if (sDEBUG) {
                printf("%c%c%c%c %10d %d", Integer.valueOf((atomType >> 24) & 255), Integer.valueOf((atomType >> 16) & 255), Integer.valueOf((atomType >> 8) & 255), Integer.valueOf((atomType >> 0) & 255), Long.valueOf(infile.position() - atomSize), Long.valueOf(atomSize));
            }
            if (atomType == FREE_ATOM || atomType == JUNK_ATOM || atomType == MDAT_ATOM || atomType == MOOV_ATOM || atomType == PNOT_ATOM || atomType == SKIP_ATOM || atomType == WIDE_ATOM || atomType == PICT_ATOM || atomType == UUID_ATOM || atomType == FTYP_ATOM) {
                if (atomSize < 8) {
                    break;
                }
            }
            printf("encountered non-QT top-level atom (is this a QuickTime file?)", new Object[0]);
            break;
        }
        if (atomType != MOOV_ATOM) {
            printf("last atom in file was not a moov atom", new Object[0]);
            return false;
        }
        int moovAtomSize = uint32ToInt(atomSize);
        long lastOffset = infile.size() - ((long) moovAtomSize);
        ByteBuffer moovAtom = ByteBuffer.allocate(moovAtomSize).order(ByteOrder.BIG_ENDIAN);
        if (!readAndFill(infile, moovAtom, lastOffset)) {
            throw new MalformedFileException("failed to read moov atom");
        } else if (moovAtom.getInt(12) == CMOV_ATOM) {
            throw new UnsupportedFileException("this utility does not support compressed moov atoms yet");
        } else {
            while (moovAtom.remaining() >= 8) {
                int atomHead = moovAtom.position();
                atomType = moovAtom.getInt(atomHead + 4);
                if (atomType != STCO_ATOM && atomType != CO64_ATOM) {
                    moovAtom.position(moovAtom.position() + 1);
                } else if (uint32ToLong(moovAtom.getInt(atomHead)) > ((long) moovAtom.remaining())) {
                    throw new MalformedFileException("bad atom size");
                } else {
                    moovAtom.position(atomHead + 12);
                    if (moovAtom.remaining() < 4) {
                        throw new MalformedFileException("malformed atom");
                    }
                    int offsetCount = uint32ToInt(moovAtom.getInt());
                    int i;
                    if (atomType == STCO_ATOM) {
                        printf("patching stco atom...", new Object[0]);
                        if (moovAtom.remaining() < offsetCount * 4) {
                            throw new MalformedFileException("bad atom size/element count");
                        }
                        i = 0;
                        while (i < offsetCount) {
                            int currentOffset = moovAtom.getInt(moovAtom.position());
                            int newOffset = currentOffset + moovAtomSize;
                            if (currentOffset >= 0 || newOffset < 0) {
                                moovAtom.putInt(newOffset);
                                i++;
                            } else {
                                throw new UnsupportedFileException("This is bug in original qt-faststart.c: stco atom should be extended to co64 atom as new offset value overflows uint32, but is not implemented.");
                            }
                        }
                        continue;
                    } else if (atomType == CO64_ATOM) {
                        printf("patching co64 atom...", new Object[0]);
                        if (moovAtom.remaining() < offsetCount * 8) {
                            throw new MalformedFileException("bad atom size/element count");
                        }
                        for (i = 0; i < offsetCount; i++) {
                            long currentOffset2 = moovAtom.getLong(moovAtom.position());
                            moovAtom.putLong(((long) moovAtomSize) + currentOffset2);
                        }
                    } else {
                        continue;
                    }
                }
            }
            infile.position(startOffset);
            if (ftypAtom != null) {
                printf("writing ftyp atom...", new Object[0]);
                ftypAtom.rewind();
                outfile.write(ftypAtom);
            }
            printf("writing moov atom...", new Object[0]);
            moovAtom.rewind();
            outfile.write(moovAtom);
            printf("copying rest of file...", new Object[0]);
            infile.transferTo(startOffset, lastOffset - startOffset, outfile);
            return true;
        }
    }
}