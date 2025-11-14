/*
The header file for the model of a two-button Simon game.

A class which represents the model part of the Simon game. It contains methods used start,
run, and end the game.

By Joseph Hamilton and Jake Heairld
March 6, 2025
*/

#ifndef MODEL_H
#define MODEL_H

#include <QObject>
#include <QTimer>
#include <vector>

using std::vector;

class Model : public QObject
{
    Q_OBJECT

public:
    /**
     * Creates a new model object
     * @param parent - the parent of the model
     */
    explicit Model(QObject *parent = nullptr);

private:
    int level;
    int currentPoints;
    int currentButton;
    bool playerTurn;
    bool playing;
    vector<int> buttonSequence;
    QTimer timer;

    /**
     * Runs the next level of the current game
     */
    void playNextLevel();

    /**
     * A helper method for handling the portion of the game which ocurrs after the
     * flashing of the pattern ends
     */
    void endSequence();

    /**
     * Handles when the user clicks the next button in the correct sequence
     */
    void correctButtonClick();

    /**
     * Ends the current game
     */
    void endGame();

public slots:
    /**
     * Starts a new round of the game
     */
    void startGame();

    /**
     * Flashes the next button in the sequence
     */
    void flashNextButton();

    /**
     * Handles the logic for when the red button is clicked
     */
    void button1Clicked();

    /**
     * Handles the logic for when the blue button is clicked
     */
    void button2Clicked();

signals:
    /**
     * Lets the view know the game has been started
     */
    void gameStarted();

    /**
     * Lets the view know the game has ended
     */
    void gameEnded();

    /**
     * Requests to enable or disable the red and blue buttons
     * @param enable - whether to enable or disable the buttons; true to enable, false to
     * disable
     */
    void enableButtons(bool enable);

    /**
     * Requests to flash a button
     * @param button - which button to flash; 0 for the right button or any other integer
     * for the blue button
     */
    void flashButton(int button);

    /**
     * Requests to update the percentage of the progress bar
     * @param percentage - the percentage to set the progress bar to
     */
    void updateProgressBar(int percentage);
};

#endif // MODEL_H
