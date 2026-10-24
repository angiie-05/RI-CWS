package uo.ri.cws.application.service.client.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;

/**
 * Conversions between the service dto and the persistence record
 */
public class ClientAssembler {

    private ClientAssembler() {
    }

    public static ClientDto toDto(ClientRecord r) {
        ClientDto dto = new ClientDto();
        dto.id = r.id;
        dto.version = r.version;
        dto.nif = r.nif;
        dto.name = r.name;
        dto.surname = r.surname;
        dto.addressStreet = r.street;
        dto.addressCity = r.city;
        dto.addressCountry = r.country;
        dto.addressZipcode = r.postalcode;
        dto.phone = r.phone;
        dto.email = r.email;
        return dto;
    }

    public static Optional<ClientDto> toDto(Optional<ClientRecord> r) {
        if (r.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toDto(r.get()));
    }

    public static List<ClientDto> toDtoList(List<ClientRecord> records) {
        List<ClientDto> res = new ArrayList<>();
        for (ClientRecord r : records) {
            res.add(toDto(r));
        }
        return res;
    }

    public static ClientRecord toRecord(ClientDto dto) {
        ClientRecord r = new ClientRecord();
        r.nif = dto.nif;
        r.name = dto.name;
        r.surname = dto.surname;
        r.street = dto.addressStreet;
        r.city = dto.addressCity;
        r.country = dto.addressCountry;
        r.postalcode = dto.addressZipcode;
        r.phone = dto.phone;
        r.email = dto.email;
        return r;
    }
}
