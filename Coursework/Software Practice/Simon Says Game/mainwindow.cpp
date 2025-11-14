/*
The implementation file for the main window of a two-button Simon game.

A class which represents the view part of the Simon game. It contains methods used to
create and update the view.

By Joseph Hamilton and Jake Heairld
March 6, 2025
*/

#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(Model *model, QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    // Set up the audio
    sfxPlayer.setAudioOutput(&sfxAudio);
    musicPlayer.setAudioOutput(&musicAudio);
    sfxAudio.setVolume(50);
    musicAudio.setVolume(20);
    musicPlayer.setLoops(QMediaPlayer::Infinite);

    // Set the background
    this->setStyleSheet(QString("background-color: rgb(30,30,30);"));

    // Set up the buttons
    ui->startButton->setStyleSheet(QString("QPushButton {background-color: rgb(0,210,0);}"));

    ui->button1->setStyleSheet(
        QString("QPushButton {background-color: rgb(200,0,0);} QPushButton:pressed "
                "{background-color: rgb(255,140,140);}"));
    ui->button2->setStyleSheet(
        QString("QPushButton {background-color: rgb(0,0,200);} QPushButton:pressed "
                "{background-color: rgb(140,140,255);}"));

    ui->progressBar->setStyleSheet(
        QString("QProgressBar {border: 1px solid rgb(210,210,210); text-align: center; "
                "background-color: rgb(210,210,210);} QProgressBar::chunk {background-color: "
                "rgb(160,255,160);}"));

    // Hide the you lose message
    ui->youLoseBox->setVisible(false);

    // From view to model
    connect(ui->startButton, &QPushButton::clicked, model, &Model::startGame);
    connect(ui->button1, &QPushButton::clicked, model, &Model::button1Clicked);
    connect(ui->button2, &QPushButton::clicked, model, &Model::button2Clicked);

    // From model to view
    connect(model, &Model::gameStarted, this, &MainWindow::gameStarted);
    connect(model, &Model::gameEnded, this, &MainWindow::gameEnded);
    connect(model, &Model::enableButtons, this, &MainWindow::enableButtons);
    connect(model, &Model::flashButton, this, &MainWindow::flashButton);
    connect(model, &Model::updateProgressBar, this, &MainWindow::updateProgressBar);

    // From view to view
    connect(ui->startButton, &QPushButton::clicked, this, &MainWindow::playClickSound);
    connect(ui->button1, &QPushButton::clicked, this, &MainWindow::playClickSound);
    connect(ui->button2, &QPushButton::clicked, this, &MainWindow::playClickSound);
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::playClickSound()
{
    sfxPlayer.setSource(QUrl("qrc:/resources/audio/click.mp3"));
    QTimer::singleShot(0, this, [this]() { sfxPlayer.play(); });
}

void MainWindow::gameStarted()
{
    musicPlayer.setSource(QUrl("qrc:/resources/audio/backgroundMusic.mp3"));
    QTimer::singleShot(0, this, [this]() { musicPlayer.play(); });

    ui->youLoseBox->setVisible(false);
    ui->startButton->setEnabled(false);
    ui->startButton->setStyleSheet(
        QString("QPushButton {background-color: rgb(210,210,210); color: rgb(0,0,0);}"));
    ui->startButton->setText("Game In Progress");
}

void MainWindow::gameEnded()
{
    sfxPlayer.setSource(QUrl("qrc:/resources/audio/loseSound.mp3"));
    QTimer::singleShot(0, this, [this]() { sfxPlayer.play(); });
    musicPlayer.setSource(QUrl("qrc:/resources/audio/loseMusic.mp3"));
    QTimer::singleShot(0, this, [this]() { musicPlayer.play(); });
    ui->startButton->setStyleSheet(
        QString("QPushButton {background-color: rgb(0,210,0); color: rgb(0,0,0);}"));

    ui->startButton->setEnabled(true);
    ui->startButton->setText("Start");
    updateProgressBar(0);
    ui->button1->setEnabled(false);
    ui->button2->setEnabled(false);
    ui->youLoseBox->setVisible(true);
}

void MainWindow::enableButtons(bool enable)
{
    ui->button1->setEnabled(enable);
    ui->button2->setEnabled(enable);
}

void MainWindow::flashButton(int button)
{
    sfxPlayer.setSource(QUrl("qrc:/resources/audio/beep.mp3"));
    QTimer::singleShot(0, this, [this]() { sfxPlayer.play(); });

    if (button == 0) {
        //light up red button
        QTimer::singleShot(0, this, [this]() {
            ui->button1->setStyleSheet(
                QString("QPushButton {background-color: rgb(255,140,140);}"));
        });

        //light out red button
        QTimer::singleShot(300, this, [this]() {
            ui->button1->setStyleSheet(
                QString("QPushButton {background-color: rgb(200,0,0);} QPushButton:pressed "
                        "{background-color: rgb(255,140,140);}"));
        });
    } else {
        //light up blue button
        QTimer::singleShot(0, this, [this]() {
            ui->button2->setStyleSheet(
                QString("QPushButton {background-color: rgb(140,140,255);}"));
        });

        //light out blue button
        QTimer::singleShot(300, this, [this]() {
            ui->button2->setStyleSheet(
                QString("QPushButton {background-color: rgb(0,0,200);} QPushButton:pressed "
                        "{background-color: rgb(140,140,255);}"));
        });
    }
}

void MainWindow::updateProgressBar(int percentage)
{
    if (percentage <= 0) {
        // Play the percentage decreasing to zero animation
        for (int i = 0; i <= 20; i++) {
            QTimer::singleShot(i * 4, this, [this]() {
                ui->progressBar->setValue(ui->progressBar->value() - 5);
            });
        }
        QTimer::singleShot(21 * 4, this, [this]() { ui->progressBar->setValue(0); });
    } else {
        ui->progressBar->setValue(percentage);
    }
}
