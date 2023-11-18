package ir.mahdi.mzip.rar.unpack.decode;

public enum CodeType {
    CODE_HUFFMAN,
    CODE_LZ,
    CODE_LZ2,
    CODE_REPEATLZ,
    CODE_CACHELZ,
    CODE_STARTFILE,
    CODE_ENDFILE,
    CODE_VM,
    CODE_VMDATA
}
