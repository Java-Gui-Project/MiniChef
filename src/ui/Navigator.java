package ui;

/** Contract for switching between screens. Each panel calls nav.showXxx() instead of touching CardLayout directly. */
public interface Navigator {
    void showMenu();
    void showDiff(String chefName);
    void showGame();
    void showResult();
    void showBoard();
}
