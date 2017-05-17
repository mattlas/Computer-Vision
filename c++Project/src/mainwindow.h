#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <string>
#include <QString>


namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT


    bool saveImages;
    std::string path;


public:
    explicit MainWindow(QWidget *parent = 0);
    bool saveImagesOnPC(bool SAVE);
    std::string QstringToString(QString value);
    ~MainWindow();
    QString qpath;

private slots:
    void on_button_browse_clicked();

    void on_button_next_clicked();

    void on_button_create_clicked();

    void on_button_next_4_clicked();

    void on_label_linkActivated(const QString &link);

    void on_pushButton_clicked();

    void on_pushButton_2_clicked();

private:
    Ui::MainWindow *ui;
};

#endif // MAINWINDOW_H
