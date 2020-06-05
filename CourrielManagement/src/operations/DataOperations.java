package operations;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class DataOperations
        implements Serializable {

    public void Write(String name, List<String> data) throws FileNotFoundException, IOException {
        FileOutputStream file = new FileOutputStream(name);
        Throwable throwable = null;
        try {
            BufferedOutputStream buffer = new BufferedOutputStream(file);
            Throwable throwable2 = null;
            try {
                ObjectOutputStream writer = new ObjectOutputStream(buffer);
                Throwable throwable3 = null;
                try {
                    writer.writeObject(data);
                } catch (IOException throwable4) {
                    throwable3 = throwable4;
                    throw throwable4;
                } finally {
                    if (writer != null) {
                        if (throwable3 != null) {
                            try {
                                writer.close();
                            } catch (IOException throwable5) {
                                throwable3.addSuppressed(throwable5);
                            }
                        } else {
                            writer.close();
                        }
                    }
                }
            } catch (IOException writer) {
                throwable2 = writer;
                throw writer;
            } finally {
                if (buffer != null) {
                    if (throwable2 != null) {
                        try {
                            buffer.close();
                        } catch (IOException writer) {
                            throwable2.addSuppressed(writer);
                        }
                    } else {
                        buffer.close();
                    }
                }
            }
        } catch (Throwable buffer) {
            throwable = buffer;
            throw buffer;
        } finally {
            if (file != null) {
                if (throwable != null) {
                    try {
                        file.close();
                    } catch (IOException buffer) {
                        throwable.addSuppressed(buffer);
                    }
                } else {
                    file.close();
                }
            }
        }
    }

    public List Read(String name) throws FileNotFoundException, IOException, ClassNotFoundException {
        List list;
        FileInputStream file = new FileInputStream(name);
        Throwable throwable = null;
        try {
            BufferedInputStream buffer = new BufferedInputStream(file);
            Throwable throwable2 = null;
            try {
                ObjectInputStream reader = new ObjectInputStream(buffer);
                Throwable throwable3 = null;
                try {
                	list = (List) reader.readObject();
                } catch (IOException | ClassNotFoundException throwable4) {
                    throwable3 = throwable4;
                    throw throwable4;
                } finally {
                    if (reader != null) {
                        if (throwable3 != null) {
                            try {
                                reader.close();
                            } catch (IOException throwable5) {
                                throwable3.addSuppressed(throwable5);
                            }
                        } else {
                            reader.close();
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException reader) {
                throwable2 = reader;
                throw reader;
            } finally {
                if (buffer != null) {
                    if (throwable2 != null) {
                        try {
                            buffer.close();
                        } catch (IOException reader) {
                            throwable2.addSuppressed(reader);
                        }
                    } else {
                        buffer.close();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException buffer) {
            throwable = buffer;
            throw buffer;
        } finally {
            if (file != null) {
                if (throwable != null) {
                    try {
                        file.close();
                    } catch (IOException buffer) {
                        throwable.addSuppressed(buffer);
                    }
                } else {
                    file.close();
                }
            }
        }
        return list;
    }

    public void Clean(String name) throws FileNotFoundException, IOException {
        List<String> list = Arrays.asList("");
        this.Write(name, list);
    }

    public void Delete(String name) {
        File file = new File(name);
        file.delete();
    }

    public String GetLine(List<String> list, String title) throws FileNotFoundException, IOException, ClassNotFoundException {
        for (String str : list) {
            if (!str.startsWith(title)) {
                continue;
            }
            return str.replaceFirst(title + "=", "");
        }
        return null;
    }

    public void Replace(List<String> list, String name, String start, long replace) throws FileNotFoundException, IOException {
        for (int i = 0; i < list.size(); ++i) {
            if (!list.get(i).startsWith(start + "=")) {
                continue;
            }
            list.set(i, start + "=" + replace);
            this.Write(name, list);
        }
    }

    public void ReplaceString(List<String> list, String name, String start, String replace) throws FileNotFoundException, IOException {
        for (int i = 0; i < list.size(); ++i) {
            if (!list.get(i).startsWith(start + "=")) {
                continue;
            }
            list.set(i, start + "=" + replace);
            this.Write(name, list);
        }
    }
}
