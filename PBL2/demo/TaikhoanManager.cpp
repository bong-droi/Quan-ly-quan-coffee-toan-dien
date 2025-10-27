#include"TaiKhoanManager.hpp"
#include <algorithm> // Để sử dụng find_if
#include <iostream>
#include <fstream>
#include <ncurses.h>
using namespace std;

void TaiKhoanManager::taiKhoansList() {
    ifstream file("/Users/miutranqt96gmail.com/Documents/Code-Document/pb2/PBL2/demo/danhsachacc.txt");
    if (!file.is_open()) {
        cerr << "Không thể mở file danhsachacc.txt, hãy chắc chắn tệp tin tồn tại và đúng đường dẫn" << endl;
        return;
    }
    string id, matKhau;
    bool isAdmin;
    while (file >> id >> matKhau >> isAdmin) {
        taiKhoans.emplace_back(id, matKhau, isAdmin);
    }
    file.close();
}
void TaiKhoanManager::luuTaiKhoans() {
    ofstream file("/Users/miutranqt96gmail.com/Documents/Code-Document/pb2/PBL2/demo/danhsachacc.txt");
    if (!file.is_open()) {
        cerr << "Không thể mở file danhsachacc.txt" << endl;
        return;
    }
    for (const auto& tk : taiKhoans) {
        file << tk.getId() << " " << tk.getMatKhau() << " " << tk.getIsAdmin() << endl;
    }
    file.close();
}

void TaiKhoanManager::taoTaiKhoan() {
    clear();
    TaiKhoan tk;
    tk.nhap(taiKhoans);
    taiKhoans.push_back(tk);
    luuTaiKhoans();
    getch();
    }


void TaiKhoanManager::xemTaiKhoans() const {
    clear();

    mvprintw(0, 0, "Danh sach tai khoan:");
    int row = 1;

    for (const auto& tk : taiKhoans) {
        mvprintw(row++, 0, "ID: %s, Mat khau: %s, Admin: %s",
                 tk.getId().c_str(), tk.getMatKhau().c_str(),
                 (tk.getIsAdmin() ? "Co" : "Khong"));
    }

    refresh();
    getch();
}

bool TaiKhoanManager::dangNhap(TaiKhoan& taiKhoanUser) const {
    string id, matKhau;
    bool valid = false;

    while (!valid) {
        clear();
        echo();
        mvprintw(2, 20, "WELOCOM TO CAT COFFEE ");
//        mvprintw(3, 2, "| ");
//        mvprintw(4, 2, "| ");
//        mvprintw(5, 2, "| ");
//        mvprintw(6, 2, "| ");
//        mvprintw(7, 2, "| ");
//        mvprintw(8, 2, "| ");
//        
//        mvprintw(2, 50, "| ");
//        mvprintw(3, 50, "| ");
//        mvprintw(4, 50, "| ");
//        mvprintw(5, 50, "| ");
//        mvprintw(6, 50, "| ");
//        mvprintw(7, 50, "| ");
//        mvprintw(7, 50, "| ");
//        mvprintw(8, 50, "| ");
//        
//        mvprintw(1, 2, "________________________________________________");
//        mvprintw(8, 2, "------------------------------------------------");

        mvprintw(4, 5, "Nhap ID: ");
        char idn[50];
        getstr(idn);
        id = string(idn);
        noecho();
        curs_set(1);

        mvprintw(5, 5, "Nhap mat khau: ");
        char pass[50];
        getstr(pass);
        matKhau = string(pass);

        for (const auto& tk : taiKhoans) {
            if (tk.getId() == id && tk.getMatKhau() == matKhau) {
                valid = true;
                taiKhoanUser = tk;
                break;
            }
        }

        if (!valid) {
            mvprintw(10, 5, "Thong tin dang nhap khong hop le, vui long thu lai!");
            getch();
        }
    }

    mvprintw(10, 5, "Dang nhap thanh cong! ");
    getch(); // Đợi người dùng nhấn phím
    return true;
}
