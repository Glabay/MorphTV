package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.block_info;
import com.frostwire.jlibtorrent.swig.block_info.block_state_t;

public final class BlockInfo {
    /* renamed from: b */
    private final block_info f19b;

    public enum BlockState {
        NONE(block_state_t.none.swigValue()),
        REQUESTED(block_state_t.requested.swigValue()),
        WRITING(block_state_t.writing.swigValue()),
        FINISHED(block_state_t.finished.swigValue()),
        UNKNOWN(-1);
        
        private final int swigValue;

        private BlockState(int i) {
            this.swigValue = i;
        }

        public int swig() {
            return this.swigValue;
        }

        public static BlockState fromSwig(int i) {
            for (BlockState blockState : (BlockState[]) BlockState.class.getEnumConstants()) {
                if (blockState.swig() == i) {
                    return blockState;
                }
            }
            return UNKNOWN;
        }
    }

    public BlockInfo(block_info block_info) {
        this.f19b = block_info;
    }

    public block_info swig() {
        return this.f19b;
    }

    public TcpEndpoint peer() {
        return new TcpEndpoint(this.f19b.peer());
    }

    public int bytesProgress() {
        return (int) this.f19b.getBytes_progress();
    }

    public int blockSize() {
        return (int) this.f19b.getBlock_size();
    }

    public BlockState state() {
        return BlockState.fromSwig((int) this.f19b.getState());
    }

    public int numPeers() {
        return (int) this.f19b.getNum_peers();
    }
}
