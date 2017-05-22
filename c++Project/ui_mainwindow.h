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
#include <QtWidgets/QProgressBar>
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
    QWidget *PickFolder;
    QPushButton *button_next;
    QLabel *foldername;
    QLabel *title_1;
    QPushButton *button_browse;
    QWidget *CopyImages;
    QPushButton *button_create;
    QRadioButton *radioButton_yes;
    QRadioButton *radioButton_no;
    QLabel *title_2;
    QWidget *Progress;
    QLabel *label_4;
    QProgressBar *progressBar;
    QWidget *MosaicSaved;
    QLabel *label_5;
    QLabel *label_7;
    QLabel *label;
    QPushButton *pushButton;
    QPushButton *pushButton_2;
    QWidget *Error;
    QLabel *label_6;
    QButtonGroup *buttonGroup_save;

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
        stackedWidget->setGeometry(QRect(0, 20, 601, 231));
        QFont font;
        font.setFamily(QStringLiteral("Noto Sans CJK KR"));
        font.setPointSize(16);
        stackedWidget->setFont(font);
        stackedWidget->setAutoFillBackground(false);
        stackedWidget->setStyleSheet(QStringLiteral("background-color: rgb(238, 238, 236);"));
        PickFolder = new QWidget();
        PickFolder->setObjectName(QStringLiteral("PickFolder"));
        button_next = new QPushButton(PickFolder);
        button_next->setObjectName(QStringLiteral("button_next"));
        button_next->setGeometry(QRect(470, 180, 85, 28));
        button_next->setStyleSheet(QLatin1String("background-color: rgb(211, 215, 207);\n"
"font: 13pt \"Noto Sans CJK KR\";\n"
"color: rgb(255, 255, 255);"));
        foldername = new QLabel(PickFolder);
        foldername->setObjectName(QStringLiteral("foldername"));
        foldername->setGeometry(QRect(110, 100, 461, 32));
        foldername->setStyleSheet(QStringLiteral("font: 16pt \"Noto Sans CJK KR\";"));
        foldername->setWordWrap(true);
        title_1 = new QLabel(PickFolder);
        title_1->setObjectName(QStringLiteral("title_1"));
        title_1->setGeometry(QRect(30, 40, 141, 35));
        title_1->setStyleSheet(QStringLiteral("font: 18pt \"Noto Sans CJK KR\";"));
        button_browse = new QPushButton(PickFolder);
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
        stackedWidget->addWidget(PickFolder);
        CopyImages = new QWidget();
        CopyImages->setObjectName(QStringLiteral("CopyImages"));
        button_create = new QPushButton(CopyImages);
        button_create->setObjectName(QStringLiteral("button_create"));
        button_create->setGeometry(QRect(470, 180, 85, 28));
        button_create->setStyleSheet(QLatin1String("background-color: rgb(0, 96, 100);\n"
"font: 12pt \"Noto Sans CJK KR\";\n"
"color: rgb(255, 255, 255);"));
        radioButton_yes = new QRadioButton(CopyImages);
        buttonGroup_save = new QButtonGroup(MainWindow);
        buttonGroup_save->setObjectName(QStringLiteral("buttonGroup_save"));
        buttonGroup_save->addButton(radioButton_yes);
        radioButton_yes->setObjectName(QStringLiteral("radioButton_yes"));
        radioButton_yes->setGeometry(QRect(40, 100, 70, 30));
        radioButton_yes->setBaseSize(QSize(0, 0));
        radioButton_yes->setStyleSheet(QStringLiteral("font: 16pt \"Noto Sans CJK KR\";"));
        radioButton_yes->setChecked(true);
        radioButton_no = new QRadioButton(CopyImages);
        buttonGroup_save->addButton(radioButton_no);
        radioButton_no->setObjectName(QStringLiteral("radioButton_no"));
        radioButton_no->setGeometry(QRect(130, 100, 70, 30));
        radioButton_no->setStyleSheet(QStringLiteral("font: 16pt \"Noto Sans CJK KR\";"));
        title_2 = new QLabel(CopyImages);
        title_2->setObjectName(QStringLiteral("title_2"));
        title_2->setGeometry(QRect(30, 40, 581, 35));
        title_2->setStyleSheet(QStringLiteral("font: 18pt \"Noto Sans CJK KR\";"));
        stackedWidget->addWidget(CopyImages);
        Progress = new QWidget();
        Progress->setObjectName(QStringLiteral("Progress"));
        label_4 = new QLabel(Progress);
        label_4->setObjectName(QStringLiteral("label_4"));
        label_4->setGeometry(QRect(20, 80, 301, 41));
        progressBar = new QProgressBar(Progress);
        progressBar->setObjectName(QStringLiteral("progressBar"));
        progressBar->setGeometry(QRect(20, 140, 561, 23));
        progressBar->setValue(24);
        stackedWidget->addWidget(Progress);
        MosaicSaved = new QWidget();
        MosaicSaved->setObjectName(QStringLiteral("MosaicSaved"));
        label_5 = new QLabel(MosaicSaved);
        label_5->setObjectName(QStringLiteral("label_5"));
        label_5->setGeometry(QRect(20, 30, 231, 51));
        label_7 = new QLabel(MosaicSaved);
        label_7->setObjectName(QStringLiteral("label_7"));
        label_7->setGeometry(QRect(380, 90, 141, 61));
        label = new QLabel(MosaicSaved);
        label->setObjectName(QStringLiteral("label"));
        label->setGeometry(QRect(270, 50, 281, 20));
        pushButton = new QPushButton(MosaicSaved);
        pushButton->setObjectName(QStringLiteral("pushButton"));
        pushButton->setGeometry(QRect(360, 200, 85, 28));
        pushButton_2 = new QPushButton(MosaicSaved);
        pushButton_2->setObjectName(QStringLiteral("pushButton_2"));
        pushButton_2->setGeometry(QRect(470, 200, 85, 28));
        stackedWidget->addWidget(MosaicSaved);
        Error = new QWidget();
        Error->setObjectName(QStringLiteral("Error"));
        label_6 = new QLabel(Error);
        label_6->setObjectName(QStringLiteral("label_6"));
        label_6->setGeometry(QRect(280, 50, 141, 161));
        stackedWidget->addWidget(Error);
        MainWindow->setCentralWidget(centralWidget);

        retranslateUi(MainWindow);

        stackedWidget->setCurrentIndex(0);


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
        label_4->setText(QApplication::translate("MainWindow", "Wait please, the images are being processed.", Q_NULLPTR));
        label_5->setText(QApplication::translate("MainWindow", "Your mosaic has been saved in", Q_NULLPTR));
        label_7->setText(QApplication::translate("MainWindow", "Do it again?", Q_NULLPTR));
        label->setText(QString());
        pushButton->setText(QApplication::translate("MainWindow", "Do it again", Q_NULLPTR));
        pushButton_2->setText(QApplication::translate("MainWindow", "QUIT", Q_NULLPTR));
        label_6->setText(QApplication::translate("MainWindow", " I am Error", Q_NULLPTR));
    } // retranslateUi

};

namespace Ui {
    class MainWindow: public Ui_MainWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_MAINWINDOW_H
