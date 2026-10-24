package uo.ri.cws.application.persistence.recommendation.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.recommendation.RecommendationGateway.RecommendationRecord;
import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;

public class RecommendationAssembler extends BaseRecordAssembler<RecommendationRecord> {

    public RecommendationAssembler() {
        super(RecommendationRecord::new);
    }

    @Override
    public RecommendationRecord toRecord(ResultSet rs) throws SQLException {
        RecommendationRecord record = super.toRecord(rs);
        record.usedforvoucher = rs.getBoolean("usedforvoucher");
        record.recommender_id = rs.getString("recommender_id");
        record.recommended_id = rs.getString("recommended_id");
        return record;
    }
}
