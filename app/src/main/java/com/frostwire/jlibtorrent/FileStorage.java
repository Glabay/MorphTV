package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.file_flags_t;
import com.frostwire.jlibtorrent.swig.file_slice_vector;
import com.frostwire.jlibtorrent.swig.file_storage;
import com.frostwire.jlibtorrent.swig.string_vector;
import com.frostwire.jlibtorrent.swig.torrent_info;
import java.io.File;
import java.util.ArrayList;

public final class FileStorage {
    public static final file_flags_t FLAG_EXECUTABLE = file_storage.flag_executable;
    public static final file_flags_t FLAG_HIDDEN = file_storage.flag_hidden;
    public static final file_flags_t FLAG_PAD_FILE = file_storage.flag_pad_file;
    public static final file_flags_t FLAG_SYMLINK = file_storage.flag_symlink;
    private final file_storage fs;
    private final torrent_info ti;

    public FileStorage(file_storage file_storage) {
        this(file_storage, null);
    }

    FileStorage(file_storage file_storage, torrent_info torrent_info) {
        this.fs = file_storage;
        this.ti = torrent_info;
    }

    public file_storage swig() {
        return this.fs;
    }

    public torrent_info ti() {
        return this.ti;
    }

    public boolean isValid() {
        return this.fs.is_valid();
    }

    public void reserve(int i) {
        this.fs.reserve(i);
    }

    public void addFile(String str, long j, file_flags_t file_flags_t, int i, String str2) {
        this.fs.add_file(str, j, file_flags_t, (long) i, str2);
    }

    public void addFile(String str, long j, file_flags_t file_flags_t, int i) {
        this.fs.add_file(str, j, file_flags_t, (long) i);
    }

    public void addFile(String str, long j, file_flags_t file_flags_t) {
        this.fs.add_file(str, j, file_flags_t);
    }

    public void addFile(String str, long j) {
        this.fs.add_file(str, j);
    }

    public void renameFile(int i, String str) {
        this.fs.rename_file(i, str);
    }

    public ArrayList<FileSlice> mapBlock(int i, long j, int i2) {
        return mapBlock(this.fs.map_block(i, j, i2));
    }

    public PeerRequest mapFile(int i, long j, int i2) {
        return new PeerRequest(this.fs.map_file(i, j, i2));
    }

    public int numFiles() {
        return this.fs.num_files();
    }

    public long totalSize() {
        return this.fs.total_size();
    }

    public int numPieces() {
        return this.fs.num_pieces();
    }

    public void numPieces(int i) {
        this.fs.set_num_pieces(i);
    }

    public int pieceLength() {
        return this.fs.piece_length();
    }

    public void pieceLength(int i) {
        this.fs.set_piece_length(i);
    }

    public int pieceSize(int i) {
        return this.fs.piece_size(i);
    }

    public String name() {
        return this.fs.name();
    }

    public void name(String str) {
        this.fs.set_name(str);
    }

    public Sha1Hash hash(int i) {
        return new Sha1Hash(this.fs.hash(i));
    }

    public String filePath(int i, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(File.separator);
        stringBuilder.append(this.fs.file_path(i));
        return stringBuilder.toString();
    }

    public String filePath(int i) {
        return this.fs.file_path(i);
    }

    public String fileName(int i) {
        return Vectors.byte_vector2string(this.fs.file_name(i).to_bytes(), "UTF-8");
    }

    public long fileSize(int i) {
        return this.fs.file_size(i);
    }

    public boolean padFileAt(int i) {
        return this.fs.pad_file_at(i);
    }

    public long fileOffset(int i) {
        return this.fs.file_offset(i);
    }

    public ArrayList<String> paths() {
        string_vector paths = this.fs.paths();
        int size = (int) paths.size();
        ArrayList<String> arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(paths.get(i));
        }
        return arrayList;
    }

    public file_flags_t fileFlags(int i) {
        return this.fs.file_flags(i);
    }

    public boolean fileAbsolutePath(int i) {
        return this.fs.file_absolute_path(i);
    }

    public int fileIndexAtOffset(long j) {
        return this.fs.file_index_at_offset(j);
    }

    static ArrayList<FileSlice> mapBlock(file_slice_vector file_slice_vector) {
        int size = (int) file_slice_vector.size();
        ArrayList<FileSlice> arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(new FileSlice(file_slice_vector.get(i)));
        }
        return arrayList;
    }
}
