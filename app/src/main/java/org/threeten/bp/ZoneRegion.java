package org.threeten.bp;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.regex.Pattern;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.zone.ZoneRules;
import org.threeten.bp.zone.ZoneRulesException;
import org.threeten.bp.zone.ZoneRulesProvider;

final class ZoneRegion extends ZoneId implements Serializable {
    private static final Pattern PATTERN = Pattern.compile("[A-Za-z][A-Za-z0-9~/._+-]+");
    private static final long serialVersionUID = 8386373296231747096L;
    private final String id;
    private final transient ZoneRules rules;

    private static ZoneRegion ofLenient(String str) {
        StringBuilder stringBuilder;
        if (!(str.equals("Z") || str.startsWith("+"))) {
            if (!str.startsWith("-")) {
                if (!(str.equals("UTC") || str.equals("GMT"))) {
                    if (!str.equals("UT")) {
                        if (!(str.startsWith("UTC+") || str.startsWith("GMT+") || str.startsWith("UTC-"))) {
                            if (!str.startsWith("GMT-")) {
                                if (!str.startsWith("UT+")) {
                                    if (!str.startsWith("UT-")) {
                                        return ofId(str, false);
                                    }
                                }
                                str = ZoneOffset.of(str.substring(2));
                                if (str.getTotalSeconds() == 0) {
                                    return new ZoneRegion("UT", str.getRules());
                                }
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("UT");
                                stringBuilder.append(str.getId());
                                return new ZoneRegion(stringBuilder.toString(), str.getRules());
                            }
                        }
                        ZoneOffset of = ZoneOffset.of(str.substring(3));
                        if (of.getTotalSeconds() == 0) {
                            return new ZoneRegion(str.substring(0, 3), of.getRules());
                        }
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(str.substring(0, 3));
                        stringBuilder2.append(of.getId());
                        return new ZoneRegion(stringBuilder2.toString(), of.getRules());
                    }
                }
                return new ZoneRegion(str, ZoneOffset.UTC.getRules());
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid ID for region-based ZoneId, invalid format: ");
        stringBuilder.append(str);
        throw new DateTimeException(stringBuilder.toString());
    }

    static ZoneRegion ofId(String str, boolean z) {
        Jdk8Methods.requireNonNull(str, "zoneId");
        if (str.length() >= 2) {
            if (PATTERN.matcher(str).matches()) {
                ZoneRules zoneRules = null;
                try {
                    zoneRules = ZoneRulesProvider.getRules(str, true);
                } catch (ZoneRulesException e) {
                    if (str.equals("GMT0")) {
                        zoneRules = ZoneOffset.UTC.getRules();
                    } else if (z) {
                        throw e;
                    }
                }
                return new ZoneRegion(str, zoneRules);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid ID for region-based ZoneId, invalid format: ");
        stringBuilder.append(str);
        throw new DateTimeException(stringBuilder.toString());
    }

    ZoneRegion(String str, ZoneRules zoneRules) {
        this.id = str;
        this.rules = zoneRules;
    }

    public String getId() {
        return this.id;
    }

    public ZoneRules getRules() {
        return this.rules != null ? this.rules : ZoneRulesProvider.getRules(this.id, false);
    }

    private Object writeReplace() {
        return new Ser((byte) 7, this);
    }

    private Object readResolve() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(7);
        writeExternal(dataOutput);
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.id);
    }

    static ZoneId readExternal(DataInput dataInput) throws IOException {
        return ofLenient(dataInput.readUTF());
    }
}
