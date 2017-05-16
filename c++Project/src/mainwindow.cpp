#include "mainwindow.h"
#include "../ui_mainwindow.h"
#include "QFileDialog"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
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
    {
         ui->foldername->setText(dir);
        ui->foldername->adjustSize();
        ui->button_next->setEnabled(true);
        ui->button_next->setStyleSheet({"background-color: rgb(0, 96, 100); font: 13pt 'Noto Sans CJK KR'; color: rgb(255, 255, 255);"});
        ui->button_next->repaint();
    }
}

void MainWindow::on_button_next_clicked()
{
    ui->stackedWidget->setCurrentIndex(1);
}

void MainWindow::on_button_create_clicked()
{
    ui->stackedWidget->setCurrentIndex(2);
}
