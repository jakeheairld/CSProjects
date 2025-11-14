/*
 * Team: Joseph Hamilton and Jake Heairld
 * Github Usernames: Fresno14576 and jakeheairld
 * Url: https://github.com/UofU-CS3505/cs3505-assignment6-jakeheairld
 * Creative Element: We added sound effects and background music.
 */

#include "mainwindow.h"

#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    QApplication::setStyle("Fusion");
    Model m;
    MainWindow w(&m);
    w.show();
    return a.exec();
}
