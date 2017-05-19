#include "mainwindow.h"
#include "../ui_mainwindow.h"
#include "QFileDialog"
#include "QButtonGroup"
#include "QString"
#include "QThread"
#include "QDir"
#include "MosaicData.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    ui->button_next->setEnabled(false);
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_button_browse_clicked()
{
    QString dir = QFileDialog::getExistingDirectory(this, tr("Open Directory"),
                                                     "/home",
                                                     QFileDialog::ShowDirsOnly
                                                     | QFileDialog::DontResolveSymlinks);
    if (!dir.isEmpty() && dir.compare("") > 0)
        //(TreeMarkupToolbox:6457): GLib-GObject-CRITICAL **: g_type_register_static: assertion 'parent_type > 0' failed, probably something is wrong here %Jakub
    {
        ui->foldername->setText(dir);
        ui->foldername->adjustSize();
        ui->button_next->setEnabled(true);
        ui->button_next->setStyleSheet({"background-color: rgb(0, 96, 100); font: 13pt 'Noto Sans CJK KR'; color: rgb(255, 255, 255);"});
        ui->button_next->repaint();
        this->path = this->QstringToString(dir);
        this->qpath = dir;
       //ui->label_path_1->setText(dir);
    }
}


bool MainWindow::saveImagesOnPC(bool g)
{
    return g;
}

std::string MainWindow::QstringToString(QString qstring_1)
{
    std::string text = qstring_1.toUtf8().constData(); //it should work but not sure, it needs some testing.
    return text;
}


void MainWindow::on_button_next_clicked()
{
    ui->stackedWidget->setCurrentIndex(1);
}

void MainWindow::on_button_create_clicked()
{
    ui->stackedWidget->setCurrentIndex(2);

    QDir dir("/home/5dv115/temp");
    if (!dir.exists()) {
        dir.mkdir("/home/5dv115/temp");
    }

    QThread *thread = new QThread;
    MosaicData *md = new MosaicData(this->path, "/home/5dv115/temp");
    md->addDirectory(this->path);
    md->moveToThread(thread);
    connect(thread, SIGNAL(started()), md, SLOT(startProcess()));
    connect(md, SIGNAL(finished()), this, SLOT(gotoPageMosaicSaved()));
    connect(md, SIGNAL(finished()), thread, SLOT(quit()));
    connect(md, SIGNAL(finished()), md, SLOT(deleteLater()));
    connect(thread, SIGNAL(finished()), thread, SLOT(deleteLater()));
    thread->start();
}

void MainWindow::gotoPageMosaicSaved()
{
    ui->stackedWidget->setCurrentIndex(3);
}

void MainWindow::on_label_linkActivated(const QString &link)
{

}

void MainWindow::on_pushButton_clicked()
{
    ui->stackedWidget->setCurrentIndex(0);
}

void MainWindow::on_pushButton_2_clicked()
{
    this->close();
}
