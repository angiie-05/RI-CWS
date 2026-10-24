package uo.ri.cws.application.ui.util;

import java.util.List;

import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.invoice.InvoicingService.PaymentMeanDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;
import uo.ri.cws.application.service.vehicletype.VehicleTypeCrudService.VehicleTypeDto;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService.WorkOrderDto;
import uo.ri.util.console.Console;

public class Printer {

    public static void printInvoice(InvoiceDto invoice) {

        Console.printf("Invoice #: %d\n", invoice.number);
        Console.printf("\tDate: %1$td/%1$tm/%1$tY\n", invoice.date);
        Console.printf("\tTotal: %.2f €\n", invoice.subtotal);
        Console.printf("\tTax amount: %.2f €\n", invoice.vatAmount);
        Console.printf("\tTotal, tax inc.: %.2f €\n", invoice.total);
        Console.printf("\tState: %s\n", invoice.state);
    }

    public static void printPaymentMeans(List<PaymentMeanDto> medios) {
        Console.println();
        Console.println("Available payment means");

        Console.printf("\t%s \t%-8.8s \t%s \n", "Id", "Type", "Acummulated");
        for (PaymentMeanDto medio : medios) {
            printPaymentMean(medio);
        }
    }

    private static void printPaymentMean(PaymentMeanDto medio) {
        Console.printf("\t%s \t%-8.8s \t%s \n"
                , medio.id
                , medio.getClass().getName()  // not the best...
                , medio.accumulated
        );
    }

    public static void printWorkOrder(WorkOrderDto rep) {

        Console.printf("\t%s \t%-40.40s \t%td/%<tm/%<tY \t%-12.12s \t%.2f\n"
                , rep.id
                , rep.description
                , rep.date
                , rep.state
                , rep.amount
        );
    }

    public static void printInvoicingWorkOrder(InvoicingWorkOrderDto wo) {

        Console.printf("\t%s \t%-40.40s \t%td/%<tm/%<tY \t%-12.12s \t%.2f\n"
                , wo.id
                , wo.description
                , wo.date
                , wo.state
                , wo.amount
        );
    }

    public static void printMechanic(MechanicDto m) {

        Console.printf("\t%s %-10.10s %-15.15s %-25.25s\n"
                , m.id
                , m.nif
                , m.name
                , m.surname
            );
    }

    public static void printClient(ClientDto c) {

        Console.printf("\t%s %-10.10s %-15.15s %-25.25s %-20.20s %-15.15s %-15.15s %-10.10s %-15.15s %s\n"
                , c.id
                , c.nif
                , c.name
                , c.surname
                , c.addressStreet
                , c.addressCity
                , c.addressCountry
                , c.addressZipcode
                , c.phone
                , c.email
            );
    }

    public static void printVoucher(VoucherDto v) {

        Console.printf("\t%-36.36s %-30.30s %10.2f € available %10.2f € accumulated\n"
                , v.code
                , v.description
                , v.availableAmount
                , v.accumulatedAmount
            );
    }

    public static void printVehicleType(VehicleTypeDto vt) {

        Console.printf("\t%s %-10.10s %5.2f €/hour %d\n"
                , vt.id
                , vt.name
                , vt.pricePerHour
                , vt.minTrainigHours
            );
    }

    public static void printWorkOrderDetail(WorkOrderDto wo) {

        Console.printf("%s for vehicle %s\n\t%-25.25s\n\t%tm/%<td/%<tY\n\t%s\n",
                wo.id
                , wo.vehicleId
                , wo.description
                , wo.date
                , wo.state
            );
    }

}
