package uo.ri.cws.application.persistence.util.assembler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import uo.ri.cws.application.persistence.Gateway.BaseRecord;

/**
 * Converts the rows of a ResultSet into records. Fills the columns common to
 * every table; subclasses fill the specific ones.
 */
public class BaseRecordAssembler<T extends BaseRecord> {

    private final Supplier<T> supplier;

    public BaseRecordAssembler(Supplier<T> supplier) {
        this.supplier = Objects.requireNonNull(supplier);
    }

    public List<T> toRecordList(ResultSet rs) throws SQLException {
        List<T> res = new ArrayList<>();
        while (rs.next()) {
            res.add(toRecord(rs));
        }
        return res;
    }

    public T toRecord(ResultSet rs) throws SQLException {
        T record = supplier.get();
        record.id = rs.getString("id");
        record.entityState = rs.getString("entityState");
        record.version = rs.getLong("version");
        record.createdAt = toLocalDateTime(rs.getTimestamp("createdAt"));
        record.updatedAt = toLocalDateTime(rs.getTimestamp("updatedAt"));
        return record;
    }

    private LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }
}
