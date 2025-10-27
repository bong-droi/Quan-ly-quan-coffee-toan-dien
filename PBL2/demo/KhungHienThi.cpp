#include "KhungHienThi.hpp"

KhungHienThi::KhungHienThi(const vector<string>& items)
    : menuItems(items), selectedItem(0) {}

void KhungHienThi::hienThiMenu() {
    initscr();
    noecho();
    curs_set(0);
    keypad(stdscr, TRUE);
    int ch;
    bool tieptuc = true;

    while (tieptuc) {
        clear();

        for (size_t i = 0; i < menuItems.size(); ++i) {
            if (i == selectedItem)
                attron(A_REVERSE);
            mvprintw(i +1, 2, menuItems[i].c_str());
            if (i == selectedItem)
                attroff(A_REVERSE);
        }

        ch = getch();
        switch (ch) {
            case KEY_UP:
                selectedItem = (selectedItem - 1 + menuItems.size()) % menuItems.size();
                break;
            case KEY_DOWN:
                selectedItem = (selectedItem + 1) % menuItems.size();
                break;
            case 10: // Phím Enter
                tieptuc = false;
                break;
        }
    }
    endwin(); 
}

int KhungHienThi::chonItem() {
    return selectedItem;
}
