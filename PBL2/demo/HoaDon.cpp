#include "HoaDon.hpp"
#include <iostream>
#include <fstream>
#include <ctime>
#include <iomanip>
using namespace std;

void HoaDon::inHoaDon(const map<string, pair<double, int>>& order) {
    double tongTien = 0;

    clear();
    int row = 0;
    mvprintw(row++, 10, "Cat Coffee");
    mvprintw(row++, 2, "54 Nguyen Luong Bang, Lien Chieu, DN");
    mvprintw(row++, 2, "Tell: 0847591269");
    mvprintw(row++, 10, " ");
    mvprintw(row++, 10, " ");

    
    mvprintw(row++, 2, "Hoa don cua ban:");
    row++;
    for (const auto& [tenMon, giaSoLuong] : order) {
        auto [gia, soLuong] = giaSoLuong;
        mvprintw(row++, 2, "%s - %.f VND x %d = %.f VND",
                 tenMon.c_str(), gia, soLuong, gia * soLuong);
        tongTien += gia * soLuong;
    }

    mvprintw(row++, 2, "---------------------------------");
    mvprintw(row++, 2, "Tong tien: %.f VND", tongTien);

    time_t baygio = time(0);
    tm *ltm = localtime(&baygio);
    mvprintw(row++, 2, "Luc: %02d:%02d:%02d %02d/%02d/%04d",
             ltm->tm_hour, ltm->tm_min, ltm->tm_sec,
             ltm->tm_mday, ltm->tm_mon + 1, ltm->tm_year + 1900);

    ofstream fileBill("/Users/miutranqt96gmail.com/Documents/Code-Document/pb2/PBL2/demo/quanlibill.txt", ios::app);
    if (fileBill.is_open()) {
        fileBill << "Hoa don luc: " << ltm->tm_hour<<":"<< ltm->tm_min<<":"<< ltm->tm_sec<<" "<<
        ltm->tm_mday<<"/"<< ltm->tm_mon + 1<<"/"<< ltm->tm_year + 1900<<endl ;
        for (const auto& [tenMon, giaSoLuong] : order) {
            auto [gia, soLuong] = giaSoLuong;
            fileBill << tenMon << " - " << gia << " VND x " << soLuong
                     << " = " << gia * soLuong << " VND" << endl;
        }
        fileBill << "Tong tien: " << tongTien << " VND" << endl << endl;
        fileBill.close();
    }
    ofstream fileDoanhThu("/Users/miutranqt96gmail.com/Documents/Code-Document/pb2/PBL2/demo/doanhthu.txt", ios::app);
       if(!fileDoanhThu.is_open()){
           cerr<<"không thể mở file thống kê"<< endl;
           return;
       }
       fileDoanhThu << "Doanh thu  " << ltm->tm_hour << ":" << ltm->tm_min << ":" << ltm->tm_sec << " " << ltm->tm_mday << "/" << 1 + ltm->tm_mon << "/" << 1900 + ltm->tm_year << " "<<tongTien << endl;
       fileDoanhThu.close();
    double doanhThuTong = 0;
    ifstream fileTongDoanhThuIn("/Users/miutranqt96gmail.com/Documents/Code-Document/pb2/PBL2/demo/TongDoanhThu.txt");
    if (fileTongDoanhThuIn.is_open()) {
        fileTongDoanhThuIn >> doanhThuTong;
        fileTongDoanhThuIn.close();
    }
    doanhThuTong += tongTien;

    ofstream fileTongDoanhThu("/Users/miutranqt96gmail.com/Documents/Code-Document/pb2/PBL2/demo/TongDoanhThu.txt", ios::trunc);
    if (fileTongDoanhThu.is_open()) {
        fileTongDoanhThu << fixed << setprecision(2) << doanhThuTong;
        fileTongDoanhThu.close();
    }
    refresh();
}
void HoaDon::xemHoaDon() const {
    ifstream file("/Users/miutranqt96gmail.com/Documents/Code-Document/pb2/PBL2/demo/quanlibill.txt");
    if (!file.is_open()) {
        mvprintw(0, 0, "Không thể mở file quanlibill.txt");
        refresh();
        return;
    }

    clear();
    int row = 0;
    string line;
    while (getline(file, line)) {
        mvprintw(row++, 0, "%s", line.c_str());
    }
    file.close();
    refresh();
    getch();
}

void HoaDon::xemDoanhThu() const {
    ifstream fileDoanhThu("/Users/miutranqt96gmail.com/Documents/Code-Document/pb2/PBL2/demo/doanhthu.txt");
    if (!fileDoanhThu.is_open()) {
        mvprintw(0, 0, "Không thể mở file doanhthu.txt");
        refresh();
        return;
    }
    clear();
    int row = 0;
    string line;
    while (getline(fileDoanhThu, line)) {
        mvprintw(row++, 0, "%s", line.c_str());
    }
    fileDoanhThu.close();

    ifstream fileTong("/Users/miutranqt96gmail.com/Documents/Code-Document/pb2/PBL2/demo/TongDoanhThu.txt");
    if (!fileTong.is_open()) {
        mvprintw(row++, 0, "Không thể mở file Tongdoanhthu.txt");
        refresh();
        return;
    }

    double doanhThuTong;
    fileTong >> doanhThuTong;
    mvprintw(row++, 0, "Doanh thu tong cong: %.f VND", doanhThuTong);
    fileTong.close();
    refresh();
    getch(); 
}
