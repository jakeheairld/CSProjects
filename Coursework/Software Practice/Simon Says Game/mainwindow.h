/*
The header file for the main window of a two-button Simon game.

A class which represents the view part of the Simon game. It contains methods used to
create and update the view.

By Joseph Hamilton and Jake Heairld
March 6, 2025
*/

#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QAudioOutput>
#include <QMainWindow>
#include <QMediaPlayer>
#include <QUrl>
#include "model.h"

using std::vector;

QT_BEGIN_NAMESPACE
namespace Ui {
class MainWindow;
}
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    /**
     * Creates the main window for the Simon game
     * @param model - the model of the game
     * @param parent - the parent of the main window
     */
    MainWindow(Model *model, QWidget *parent = nullptr);

    /**
     * Deconstructor for the main window
     */
    ~MainWindow();

private:
    Ui::MainWindow *ui;
    QAudioOutput sfxAudio;
    QAudioOutput musicAudio;
    QMediaPlayer sfxPlayer;
    QMediaPlayer musicPlayer;

    /**
     * Plays a click sound
     */
    void playClickSound();

public slots:
    /**
     * Starts a new round of the game
     */
    void gameStarted();

    /**
     * Ends the current round of the game
     */
    void gameEnded();

    /**
     * Enables or disables the red and blue buttons
     * @param enable - whether to enable or disable the buttons; true to enable, false to
     * disable
     */
    void enableButtons(bool enable);

    /**
     * Flashes a button for 300 ms
     * @param button - which button to flash; 0 for the right button or any other integer
     * for the blue button
     */
    void flashButton(int button);

    /**
     * Updates the percentage of the progress bar
     * @param percentage - the percentage to set the progress bar to
     */
    void updateProgressBar(int percentage);
};

#endif // MAINWINDOW_H
