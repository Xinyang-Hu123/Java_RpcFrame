package com.xinyang.rpc.remoting.protocol;

public enum MessageType {

    REQUEST((byte) 1),
    RESPONSE((byte) 2),
    HEARTBEAT((byte) 3);

    private final byte type;

    MessageType(byte type) {
        this.type = type;
    }
    public byte getType() {
        return type;
    }

}
