package uo.ri.cws.application.service.paymentmean.voucher;

import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.voucher.VoucherGateway.VoucherRecord;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;

/**
 * Conversions between the voucher service dto and the persistence record
 */
public class VoucherAssembler {

    private VoucherAssembler() {
    }

    public static VoucherDto toDto(VoucherRecord v) {
        VoucherDto dto = new VoucherDto();
        dto.id = v.id;
        dto.version = v.version;
        dto.clientId = v.client_id;
        dto.accumulatedAmount = v.accumulated;
        dto.code = v.code;
        dto.description = v.description;
        dto.availableAmount = v.available;
        return dto;
    }

    public static List<VoucherDto> toDtoList(List<VoucherRecord> records) {
        List<VoucherDto> res = new ArrayList<>();
        for (VoucherRecord v : records) {
            res.add(toDto(v));
        }
        return res;
    }
}
