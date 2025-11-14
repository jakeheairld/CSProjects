/*
The implementation file for the model of a two-button Simon game.

A class which represents the model part of the Simon game. It contains methods used start,
run, and end the game.

By Joseph Hamilton and Jake Heairld
March 6, 2025
*/

#include "model.h"
#include "mainwindow.h"

Model::Model(QObject *parent)
    : QObject{parent}
{
    connect(&timer, &QTimer::timeout, this, &Model::flashNextButton);
}

void Model::playNextLevel()
{
    level++;
    currentPoints = 0;
    currentButton = 0;
    int nextButton = rand() % 2;
    buttonSequence.push_back(nextButton);
    emit enableButtons(false);
    double flashInterval = 1000 - (level * level * 10);
    if ((flashInterval < 350) || (flashInterval > 1000))
        flashInterval = 350;
    timer.start(flashInterval);
}

void Model::endSequence()
{
    timer.stop();
    emit updateProgressBar(0);
    emit enableButtons(true);
}

void Model::correctButtonClick()
{
    currentPoints++;
    emit updateProgressBar((int) (((double) currentPoints / level) * 100));
    if (currentPoints >= level)
        playNextLevel();
}

void Model::endGame()
{
    emit gameEnded();
}

void Model::startGame()
{
    playing = true;
    emit gameStarted();
    buttonSequence = {};
    level = 0;
    Model::playNextLevel();
}

void Model::flashNextButton()
{
    if (currentButton < level) {
        emit flashButton(buttonSequence[currentButton]);
        currentButton++;
    } else {
        endSequence();
    }
}

void Model::button1Clicked()
{
    buttonSequence[currentPoints] == 0 ? Model::correctButtonClick() : Model::endGame();
}

void Model::button2Clicked()
{
    buttonSequence[currentPoints] == 1 ? Model::correctButtonClick() : Model::endGame();
}
