#pragma once
#include <SDL2_bgi.h>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include <cstring>
#include <cstdlib>
#include <cmath>
#include <ctime>
#include <strings.h>
using namespace std;
#undef getch
inline int archive_getch() {
    int k = bgi_getch();
    switch(k) {
        case SDLK_LEFT: return 75; case SDLK_RIGHT: return 77;
        case SDLK_UP: return 72; case SDLK_DOWN: return 80;
        case QUIT: case SDLK_ESCAPE: closegraph(); std::exit(0);
        default: return k;
    }
}
#define getch archive_getch
inline char* itoa(int value, char* buffer, int base) { if(base!=10) throw std::runtime_error("Only decimal conversion is used by these projects"); std::sprintf(buffer,"%d",value); return buffer; }
inline int strcmpi(const char* a,const char* b) { return strcasecmp(a,b); }
inline void getdate(date* d) { std::time_t now=std::time(nullptr); auto t=std::localtime(&now); d->da_year=1900+t->tm_year;d->da_day=t->tm_mday;d->da_mon=t->tm_mon+1; }
inline int console_x=0,console_y=0;
inline void gotoxy(int x,int y) { console_x=x-1;console_y=y-1; }
inline void clrscr() { setbkcolor(WHITE);cleardevice();setcolor(BLACK);console_x=console_y=0; }
inline void archive_putchar(char c) {
    if(c=='\r') {console_x=0;return;}
    if(c=='\n') {console_x=0;++console_y;return;}
    if(console_x>=80){console_x=0;++console_y;}
    if(console_y>=30){clrscr();}
    textsettingstype previous; gettextsettings(&previous);
    settextstyle(DEFAULT_FONT,HORIZ_DIR,1);
    char s[]={c,0};outtextxy(console_x*8,console_y*16,s);++console_x;
    settextstyle(previous.font,previous.direction,previous.charsize);
}
class ArchiveOutput : public std::streambuf {
    int_type overflow(int_type c) override { if(c!=traits_type::eof()) archive_putchar((char)c);return traits_type::not_eof(c); }
    int sync() override {refresh();return 0;}
};
inline std::string archive_readline() {
    std::string s;
    refresh();
    for(;;) {
        int c=archive_getch();
        if(c==13 || c==10){archive_putchar('\n');return s;}
        if(c==8 && !s.empty()) {s.pop_back();console_x=std::max(0,console_x-1);setfillstyle(SOLID_FILL,getbkcolor());bar(console_x*8,console_y*16,console_x*8+8,console_y*16+16);}
        else if(c>=32 && c<127) {s+=(char)c;archive_putchar((char)c);}
        refresh();
    }
}
class ArchiveInput : public std::streambuf {
    std::string line;
    int_type underflow() override {
        if(gptr() && gptr()<egptr())return traits_type::to_int_type(*gptr());
        line=archive_readline()+"\n";setg(line.data(),line.data(),line.data()+line.size());return traits_type::to_int_type(*gptr());
    }
};
inline ArchiveOutput archive_output_buffer;
inline ArchiveInput archive_input_buffer;
inline std::ostream archive_cout(&archive_output_buffer);
inline std::istream archive_cin(&archive_input_buffer);
template<size_t N> inline char* archive_gets(char (&s)[N]) { std::string line; std::getline(archive_cin>>std::ws,line);std::strncpy(s,line.c_str(),N-1);s[N-1]=0;return s; }
#define cout archive_cout
#define cin archive_cin
#define gets archive_gets

inline void archive_delay(int ms) { refresh();delay(ms); }
#define delay archive_delay
