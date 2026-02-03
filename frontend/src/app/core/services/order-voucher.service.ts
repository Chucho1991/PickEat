import { Injectable } from '@angular/core';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

export interface VoucherLineItem {
  name: string;
  unitPrice: number;
  quantity: number;
  total: number;
}

export interface VoucherDiscountLine {
  name: string;
  type: string;
  unitValue: string;
  total: number;
}

export interface VoucherData {
  date: Date;
  orderNumber: string;
  mesa: string;
  mesero: string;
  canal: string;
  currencySymbol: string;
  items: VoucherLineItem[];
  discountsApplied: VoucherDiscountLine[];
  subtotal: number;
  discountAmount: number;
  tipAmount: number;
  totalAmount: number;
  billingFields: string[];
}

/**
 * Servicio para generar vouchers PDF de ordenes.
 */
@Injectable({ providedIn: 'root' })
export class OrderVoucherService {
  /**
   * Genera y descarga el voucher PDF de la orden.
   */
  generateVoucher(data: VoucherData) {
    const doc = new jsPDF({ unit: 'pt', format: 'a4' });
    const marginX = 36;
    let y = 36;

    doc.setFontSize(14);
    doc.text('Voucher de compra', marginX, y);
    y += 18;

    doc.setFontSize(10);
    doc.text(`Fecha y Hora: ${this.formatDate(data.date)}`, marginX, y);
    y += 14;
    doc.text(`Numero orden: ${data.orderNumber}`, marginX, y);
    y += 14;
    doc.text(`Mesa: ${data.mesa}`, marginX, y);
    y += 14;
    doc.text(`Nombre mesero: ${data.mesero}`, marginX, y);
    y += 14;
    doc.text(`Canal: ${data.canal}`, marginX, y);
    y += 10;

    autoTable(doc, {
      startY: y + 10,
      head: [['Producto', 'V. Unit', 'Cant', 'V. Total']],
      body: data.items.map((line) => [
        line.name,
        this.formatMoney(line.unitPrice, data.currencySymbol),
        String(line.quantity),
        this.formatMoney(line.total, data.currencySymbol)
      ]),
      styles: { fontSize: 9 },
      headStyles: { fillColor: [241, 245, 249], textColor: 20 },
      margin: { left: marginX, right: marginX }
    });

    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const lastTable = (doc as any).lastAutoTable;
    y = (lastTable?.finalY ?? y) + 16;

    autoTable(doc, {
      startY: y + 10,
      head: [],
      body: [
        ['Subtotal', this.formatMoney(data.subtotal, data.currencySymbol)],
        ['Descuentos aplicados', `-${this.formatMoney(data.discountAmount, data.currencySymbol)}`],
        ['Propina', this.formatMoney(data.tipAmount, data.currencySymbol)],
        ['Total', this.formatMoney(data.totalAmount, data.currencySymbol)]
      ],
      styles: { fontSize: 10, cellPadding: { top: 2, right: 6, bottom: 2, left: 0 } },
      columnStyles: {
        0: { halign: 'left' },
        1: { halign: 'right', textColor: 20 }
      },
      didParseCell: (cell) => {
        if (cell.section === 'body' && cell.row.index === 1 && cell.column.index === 1) {
          cell.cell.styles.textColor = [5, 150, 105];
        }
        if (cell.section === 'body' && cell.row.index === 3) {
          cell.cell.styles.fontStyle = 'bold';
          cell.cell.styles.fontSize = 12;
        }
      },
      tableLineWidth: 0,
      margin: { left: marginX, right: marginX }
    });

    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const totalsTable = (doc as any).lastAutoTable;
    y = (totalsTable?.finalY ?? y) + 18;

    if (data.discountsApplied.length > 0) {
      y += 10;
      doc.setFontSize(10);
      doc.text('Detalle de descuentos aplicados', marginX, y);
      y += 8;
      autoTable(doc, {
        startY: y + 6,
        head: [['Descuento', 'Tipo', 'Valor', 'Total']],
        body: data.discountsApplied.map((line) => [
          line.name,
          line.type,
          line.unitValue,
          this.formatMoney(line.total, data.currencySymbol)
        ]),
        styles: { fontSize: 9 },
        headStyles: { fillColor: [241, 245, 249], textColor: 20 },
        margin: { left: marginX, right: marginX }
      });
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const discountTable = (doc as any).lastAutoTable;
      y = (discountTable?.finalY ?? y) + 14;
    }

    doc.setFontSize(10);
    doc.text('Datos para facturacion', marginX, y);
    y += 14;
    data.billingFields.forEach((label) => {
      doc.text(`${label}:`, marginX, y);
      doc.line(marginX + 90, y + 2, marginX + 320, y + 2);
      y += 14;
    });

    doc.save(`voucher-orden-${data.orderNumber}.pdf`);
  }

  private formatDate(date: Date) {
    return new Intl.DateTimeFormat('es-CO', {
      dateStyle: 'short',
      timeStyle: 'short'
    }).format(date);
  }

  private formatMoney(value: number, symbol: string) {
    const formatted = new Intl.NumberFormat('es-CO', { minimumFractionDigits: 2, maximumFractionDigits: 2 }).format(value);
    return `${symbol}${formatted}`;
  }
}
