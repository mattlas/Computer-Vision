/********************************************************************************
** Form generated from reading UI file 'mainwindow.ui'
**
** Created by: Qt User Interface Compiler version 5.8.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_MAINWINDOW_H
#define UI_MAINWINDOW_H

#include <QtCore/QVariant>
#include <QtWidgets/QAction>
#include <QtWidgets/QApplication>
#include <QtWidgets/QButtonGroup>
#include <QtWidgets/QHeaderView>
#include <QtWidgets/QLabel>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QRadioButton>
#include <QtWidgets/QStackedWidget>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_MainWindow
{
public:
    QWidget *centralWidget;
    QStackedWidget *stackedWidget;
    QWidget *page;
    QPushButton *button_next;
    QLabel *foldername;
    QLabel *title_1;
    QPushButton *button_browse;
    QWidget *page_2;
    QPushButton *button_create;
    QRadioButton *radioButton_yes;
    QRadioButton *radioButton_no;
    QLabel *title_2;
    QWidget *page_3;
    QLabel *label_4;
    QWidget *page_4;
    QLabel *label_5;
    QLabel *label_7;
    QWidget *page_5;
    QLabel *label_6;

    void setupUi(QMainWindow *MainWindow)
    {
        if (MainWindow->objectName().isEmpty())
            MainWindow->setObjectName(QStringLiteral("MainWindow"));
        MainWindow->resize(600, 250);
        QPalette palette;
        QBrush brush(QColor(0, 96, 100, 255));
        brush.setStyle(Qt::SolidPattern);
        palette.setBrush(QPalette::Active, QPalette::Button, brush);
        palette.setBrush(QPalette::Inactive, QPalette::Button, brush);
        palette.setBrush(QPalette::Disabled, QPalette::Button, brush);
        MainWindow->setPalette(palette);
        centralWidget = new QWidget(MainWindow);
        centralWidget->setObjectName(QStringLiteral("centralWidget"));
        stackedWidget = new QStackedWidget(centralWidget);
        stackedWidget->setObjectName(QStringLiteral("stackedWidget"));
        stackedWidget->setGeometry(QRect(0, 0, 600, 250));
        QFont font;
        font.setFamily(QStringLiteral("Noto Sans CJK KR"));
        font.setPointSize(16);
        stackedWidget->setFont(font);
        stackedWidget->setAutoFillBackground(false);
        stackedWidget->setStyleSheet(QStringLiteral("background-color: rgb(238, 238, 236);"));
        page = new QWidget();
        page->setObjectName(QStringLiteral("page"));
        button_next = new QPushButton(page);
        button_next->setObjectName(QStringLiteral("button_next"));
        button_next->setGeometry(QRect(470, 180, 85, 28));
        button_next->setStyleSheet(QLatin1String("background-color: rgb(211, 215, 207);\n"
"font: 13pt \"Noto Sans CJK KR\";\n"
"color: rgb(255, 255, 255);"));
        foldername = new QLabel(page);
        foldername->setObjectName(QStringLiteral("foldername"));
        foldername->setGeometry(QRect(110, 100, 461, 32));
        foldername->setStyleSheet(QStringLiteral("font: 16pt \"Noto Sans CJK KR\";"));
        foldername->setWordWrap(true);
        title_1 = new QLabel(page);
        title_1->setObjectName(QStringLiteral("title_1"));
        title_1->setGeometry(QRect(30, 40, 141, 35));
        title_1->setStyleSheet(QStringLiteral("font: 18pt \"Noto Sans CJK KR\";"));
        button_browse = new QPushButton(page);
        button_browse->setObjectName(QStringLiteral("button_browse"));
        button_browse->setGeometry(QRect(30, 85, 64, 64));
        button_browse->setCursor(QCursor(Qt::PointingHandCursor));
        button_browse->setStyleSheet(QLatin1String("background-color: rgb(238, 238, 236);\n"
"border: none;\n"
"font: 12pt \"Noto Sans CJK KR\";\n"
""));
        QIcon icon;
        icon.addFile(QStringLiteral(":/icons/folder_icon.png"), QSize(), QIcon::Normal, QIcon::Off);
        button_browse->setIcon(icon);
        button_browse->setIconSize(QSize(64, 64));
        stackedWidget->addWidget(page);
        page_2 = new QWidget();
        page_2->setObjectName(QStringLiteral("page_2"));
        button_create = new QPushButton(page_2);
        button_create->setObjectName(QStringLiteral("button_create"));
        button_create->setGeometry(QRect(470, 180, 85, 28));
        button_create->setStyleSheet(QLatin1String("background-color: rgb(0, 96, 100);\n"
"font: 12pt \"Noto Sans CJK KR\";\n"
"color: rgb(255, 255, 255);"));
        radioButton_yes = new QRadioButton(page_2);
        radioButton_yes->setObjectName(QStringLiteral("radioButton_yes"));
        radioButton_yes->setGeometry(QRect(40, 100, 70, 30));
        radioButton_yes->setBaseSize(QSize(0, 0));
        radioButton_yes->setStyleSheet(QStringLiteral("font: 16pt \"Noto Sans CJK KR\";"));
        radioButton_yes->setChecked(true);
        radioButton_no = new QRadioButton(page_2);
        radioButton_no->setObjectName(QStringLiteral("radioButton_no"));
        radioButton_no->setGeometry(QRect(130, 100, 70, 30));
        radioButton_no->setStyleSheet(QStringLiteral("font: 16pt \"Noto Sans CJK KR\";"));
        title_2 = new QLabel(page_2);
        title_2->setObjectName(QStringLiteral("title_2"));
        title_2->setGeometry(QRect(30, 40, 581, 35));
        title_2->setStyleSheet(QStringLiteral("font: 18pt \"Noto Sans CJK KR\";"));
        stackedWidget->addWidget(page_2);
        page_3 = new QWidget();
        page_3->setObjectName(QStringLiteral("page_3"));
        label_4 = new QLabel(page_3);
        label_4->setObjectName(QStringLiteral("label_4"));
        label_4->setGeometry(QRect(250, 120, 151, 91));
        stackedWidget->addWidget(page_3);
        page_4 = new QWidget();
        page_4->setObjectName(QStringLiteral("page_4"));
        label_5 = new QLabel(page_4);
        label_5->setObjectName(QStringLiteral("label_5"));
        label_5->setGeometry(QRect(160, 70, 141, 71));
        label_7 = new QLabel(page_4);
        label_7->setObjectName(QStringLiteral("label_7"));
        label_7->setGeometry(QRect(380, 110, 141, 61));
        stackedWidget->addWidget(page_4);
        page_5 = new QWidget();
        page_5->setObjectName(QStringLiteral("page_5"));
        label_6 = new QLabel(page_5);
        label_6->setObjectName(QStringLiteral("label_6"));
        label_6->setGeometry(QRect(280, 50, 141, 161));
        stackedWidget->addWidget(page_5);
        MainWindow->setCentralWidget(centralWidget);

        retranslateUi(MainWindow);

        QMetaObject::connectSlotsByName(MainWindow);
    } // setupUi

    void retranslateUi(QMainWindow *MainWindow)
    {
        MainWindow->setWindowTitle(QApplication::translate("MainWindow", "Tree mApp", Q_NULLPTR));
        button_next->setText(QApplication::translate("MainWindow", "NEXT", Q_NULLPTR));
        foldername->setText(QApplication::translate("MainWindow", "Choose a folder of images to create a mosaic", Q_NULLPTR));
        title_1->setText(QApplication::translate("MainWindow", "Pick a folder", Q_NULLPTR));
        button_browse->setText(QString());
        button_create->setText(QApplication::translate("MainWindow", "CREATE", Q_NULLPTR));
        radioButton_yes->setText(QApplication::translate("MainWindow", "Yes", Q_NULLPTR));
        radioButton_no->setText(QApplication::translate("MainWindow", "No", Q_NULLPTR));
        title_2->setText(QApplication::translate("MainWindow", "Do you want to copy the images to the computer?", Q_NULLPTR));
        label_4->setText(QApplication::translate("MainWindow", "Progress thingy", Q_NULLPTR));
        label_5->setText(QApplication::translate("MainWindow", "We did it!", Q_NULLPTR));
        label_7->setText(QApplication::translate("MainWindow", "Do it again?", Q_NULLPTR));
        label_6->setText(QApplication::translate("MainWindow", " I am Error", Q_NULLPTR));
    } // retranslateUi

};

namespace Ui {
    class MainWindow: public Ui_MainWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_MAINWINDOW_H
