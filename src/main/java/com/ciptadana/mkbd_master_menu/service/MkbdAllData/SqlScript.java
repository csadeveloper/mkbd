package com.ciptadana.mkbd_master_menu.service.MkbdAllData;

public record SqlScript(String type, String vd, String baris, String sql) {

    public String key() {
        return type + ":" + vd + (baris == null ? "" : "/" + baris);
    }

    public Integer leadingLine() {
        if (baris == null) return null;
        String body = baris.startsWith("baris") ? baris.substring(5) : baris;
        int sep = body.indexOf('_');
        String head = sep < 0 ? body : body.substring(0, sep);
        try {
            return Integer.parseInt(head);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}