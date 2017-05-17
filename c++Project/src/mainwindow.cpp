#include "mainwindow.h"
#include "../ui_mainwindow.h"
#include "QFileDialog"
#include "QButtonGroup"
#include "QString"

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
        //(TreeMarkupToolbox:28289): GLib-CRITICAL **: g_once_init_leave: assertion 'result != 0' failed, probably something is wrong here %Jakub
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

    if(ui->radioButton_yes->isChecked())
    {
    this->saveImages=saveImagesOnPC(1);
    }

    if(ui->radioButton_no->isChecked())
    {
    this->saveImages=saveImagesOnPC(0);
    }

}



void MainWindow::on_button_next_4_clicked()
{
    ui->stackedWidget->setCurrentIndex(3);

   // if (this->qpath.isEmpty()!=0)
   // {
    ui->label->setText(this->qpath);
    ui->label->repaint();
    //}
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
