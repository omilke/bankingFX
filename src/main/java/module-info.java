module de.omilke.bankingfx {

    requires kotlin.stdlib;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.swing;
    requires de.saxsys.mvvmfx;
    requires org.controlsfx.controls;
    requires de.jensd.fx.fontawesomefx.commons;
    requires de.jensd.fx.fontawesomefx.fontawesome;
    requires com.h2database;
    requires org.jooq;
    requires org.jooq.meta;
    requires java.sql;
    requires org.apache.commons.lang3;
    requires org.apache.commons.io;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    exports de.omilke.bankingfx to javafx.graphics;
    exports de.omilke.banking.persistence.jooq.meta.tables.records to org.jooq;

    opens de.omilke.bankingfx.main to de.saxsys.mvvmfx, javafx.fxml;
    opens de.omilke.bankingfx.main.entrylist to de.saxsys.mvvmfx, javafx.fxml;
    opens de.omilke.bankingfx.report to de.saxsys.mvvmfx, javafx.fxml;
    opens de.omilke.bankingfx.misc to de.saxsys.mvvmfx, javafx.fxml;
    opens de.omilke.bankingfx.report.categories to de.saxsys.mvvmfx, javafx.fxml;
    opens de.omilke.bankingfx.report.savings to de.saxsys.mvvmfx, javafx.fxml;
    opens de.omilke.bankingfx.report.balance to de.saxsys.mvvmfx, javafx.fxml;
    opens de.omilke.bankingfx.report.fortunehistory to de.saxsys.mvvmfx, javafx.fxml;
    opens de.omilke.bankingfx.report.audit to de.saxsys.mvvmfx, javafx.fxml;
    opens de.omilke.bankingfx.main.converter to de.saxsys.mvvmfx, javafx.fxml;
    opens de.omilke.bankingfx.recurringentries to de.saxsys.mvvmfx, javafx.fxml;
    opens de.omilke.bankingfx.controls to de.saxsys.mvvmfx, javafx.fxml;
}