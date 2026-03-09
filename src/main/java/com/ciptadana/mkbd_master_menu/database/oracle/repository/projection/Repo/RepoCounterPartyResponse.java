package com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Repo;

import java.util.Date;

public interface RepoCounterPartyResponse {
    String getCode();
    String getOldCode();
    String getName();
    String getTaxId();
    String getNpwp();
    String getForeigner();
    String getAffiliated();
    String getClientPph();
    String getGender();
    String getBirthPlace();
    Date getBirthDate();
    Date getMemberSince();
    String getPhysicalAddress1();
    String getPhysicalAddress2();
    String getPhysicalAddress3();
    String getPhysicalCity();
    String getPhysicalState();
    String getPhysicalZipcode();
    String getMailingName();
    String getMailingAddress1();
    String getMailingAddress2();
    String getMailingAddress3();
    String getMailingCity();
    String getMailingState();
    String getMailingZipcode();
    String getContactHome();
    String getContactOffice();
    String getContactFax();
    String getContactOther();
    String getContactEmail();
    String getBankAccountName();
    String getBankAccountNo();
    String getBankName();
    String getBankBranch();
    String getSalesman();
    String getBranch();
    String getClientType();
    String getClientGroup();
    String getCitizen();
    String getDomisili();
    String getInvestorType();
    String getConfirmation();
    String getClosuredBy();
    Date getClosuredDate();
    Date getModifiedDate();
    String getModifiedBy();
    String getReport();
    String getCardId();
    String getExpCardId();
    String getPasportId();
    String getKseiSubRek();
    String getSid();
    String getClientDoc();
    String getIndustryType();
    String getTaxStatus();

    // Joined columns
    String getClientTypeName();
    String getSalesmanName();
    String getSuspended();
}
