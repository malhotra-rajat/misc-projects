/*-------------------------------------------------------------------------*/
//               PROJECT ON RESTAURANT BILLING
/*-------------------------------------------------------------------------*/


//-------------------------By ROHIT & RAJAT--------------------------------*/
//------------------------------XII - B------------------------------------*/


/*-------------------------------------------------------------------------*/
//                HEADER FILES
/*-------------------------------------------------------------------------*/

#include<fstream.h>
#include<conio.h>
#include<graphics.h>
#include<dos.h>
#include<string.h>
#include<stdio.h>
#include<stdlib.h>

/*-------------------------------------------------------------------------*/
//               GLOBAL VARIABLES
/*-------------------------------------------------------------------------*/

    int maxy;
    int maxx;
    int y_cord=190;
    int mod_cord=190;
    int choice;
    int ch_md=0;
    char pass_o[10];
    char pass_e[10];
    int y=1;
    float bill = 0.0;

/*-------------------------------------------------------------------------*/
//                FILES USED
/*-------------------------------------------------------------------------*/

//    1: rlist.dat
//    2: sales.dat
//    3: ownerp.dat
//    4: empp.dat

/*-------------------------------------------------------------------------*/
//             FUNCTION DECLARATIONS
/*-------------------------------------------------------------------------*/


void gap(int);
void set_graph();
void intro();
void emp_screen();
void owner_screen();
void major_code();
void add_item();
void modify_item();
void delete_item();
void display_rlist();
void mod_item();
void instructions();
void view_sales();
void change_password();
void mod_screen();
void change_password();
void create_bill();
void save_sales();
void display_sales();
void icon();
void get_pass_frm_file();
void instructions_for_emp();
void verify_pass_screen();

/*-------------------------------------------------------------------------*/
//                CLASSES
/*-------------------------------------------------------------------------*/
class item
{
    char item_name[25];
    int item_code;
    float item_price;

    public:

    void get()                // FUNCTION TO GET DATA
    {
        clrscr();
        cleardevice();

        cout<<"\nItem Name    :    ";
        gets(item_name);
        gap(1);
        cout<<"Item Price    :       Rs. ";
        cin>>item_price;
        gap(1);
        cout<<"Item Code    :    ";
        cin>>item_code;
        gap(1);
    }

    void display()            // FUNCTION TO DISPLAY DATA
    {
        y++;

        cout<<endl;

        gotoxy(6,y);
        cout<<item_code;
        gotoxy(32,y);
        cout<<item_name;
        gotoxy(71,y);
	cout<<"Rs. "<<item_price;
    }




    int ret()                            // FUNCTION TO RETURN item_code
    {                                    // A PRIVATE MEMBER
        return item_code;
    }


    void modify()                        // FUNCTION TO MODIFY DATA
    {
        item b;
        b.get();
        strcpy(item_name,b.item_name);
        item_code=b.item_code;
        item_price=b.item_price;
    }

    float ret_price()                    // FUNCTION TO RETURN item_price
    {                                    // A PRIVATE MEMBER
        return item_price;
    }

    char* ret_name()                     // FUNCTION TO RETURN item_price
    {                                    // A PRIVATE MEMBER
        return item_name;
    }

};


class sales
{
    public:

    float sale;
    date d;

    void show_data ()                  // FUNCTION TO DISPLAY DATA
    {
        int a = d.da_day;
        int b = d.da_mon;

        cout<<"         ";
        cout<<a<<"/";
        cout<<b<<"/";
        cout<<d.da_year;
        cout<<"                   ";
        cout<<"Rs. "<<sale<<endl;
    }
};

/*-------------------------------------------------------------------------*/
//               THE MAIN FUNCTION
/*-------------------------------------------------------------------------*/


void main()
{
      set_graph();                        // INITIALISING GRAPHICS
      get_pass_frm_file();                // READING PASSWORD FROM FILE
      clrscr();
      cleardevice();
      icon();                             // WELCOME SCREEN
      major_code();                       // MAJOR SEGMENT OF THE PROGRAM

}

/*-------------------------------------------------------------------------*/
//            MAIN SEGMENT OF THE PROGRAM
/*-------------------------------------------------------------------------*/
void major_code()
{
    maxy = getmaxy();

    int i = 0, f = 0;       // COUNTERS TO CHECK AND ENTER PASSWORD
    char p[10],x;            // VARIABLES TO STORE PASSWORD

    beg:

    cleardevice();
    intro();

    for(;;)              // READING PASSWORD
    {
        x=getch();

        if (x!=13 && x!=8 && i<9 && x!=72 && x!=75 && x!=77 && x!=80)
        // CONTROLLING THE RANGE OF PASSWORD
        {
            p[i]=x;
            i++;
            outtextxy(179+(23.5*i),(maxy/2)+10,"*");
        }

        else

        if (x == 8 && i>0)   //  IF BACKSPACE KEY IS PRESSED
        {

            setfillstyle(SOLID_FILL,YELLOW);
            bar(200, maxy/2 +30, 410, maxy/2+70);
            setcolor(RED);
            rectangle (200, maxy/2 +30, 410, maxy/2+70);


            i--;         // DECREASING THE INDEX


            for(f=0;f<i;)   // DISPLAYIG * IN GRAPHICS MODE
            {
                f++;
                outtextxy(179+(23.5*f),(maxy/2)+10,"*");
            }

        }

        else

        if (x==13)             // IF ENTER IS PRESSED
        {
            p[i]='\0';
            break;
        }

    }            //    END OF FOR LOOP

    f = 0;
    i = 0;

    verify_pass_screen();   // ANIMATION

    if (strcmpi (p,pass_o)==0 )    // COMPARING PASSWORD
    {
        prev_scr:
        cleardevice();
        y_cord=190;
        owner_screen();
        int key;

        for(;;)
        {
          key=getch();

          if(key!=13)
          {
            if(key==72)           // IF UP KEY IS PRESSED
            {
                setcolor(YELLOW);
                outtextxy (maxx/2-150, y_cord-50, "*");
                setcolor(RED);
                y_cord-=50;   // * MOVES ONE POSITION UP

                if(y_cord<190)
                    y_cord=190;

                outtextxy (maxx/2-150, y_cord-50, "*");
             }

             else if(key==80)
             {
                setcolor(YELLOW);
                outtextxy (maxx/2-150, y_cord-50, "*");
                setcolor(RED);
                y_cord+=50;       // * MOVES ONE POSITION DOWN

                if(y_cord>440)
                    y_cord=440;

                outtextxy (maxx/2-150, y_cord-50, "*");
             }
          }

          if(key==13)        // IF ENTER IS PRESSED, LOOP BREAKS

               break;

        }              // END OF FOR LOOP



        switch ( y_cord )         // CHOICES FOR OWNER
        {

            case 190: choice=1;
                  break;

            case 240: choice=2;
                  break;

            case 290: choice=3;
                  break;

            case 340: choice=4;
                  break;

            case 390: goto beg;    // LOG OFF OPTION

            case 440: exit(0);

        }


        switch(choice)    // FUNCTIONS CALLED ON THE BASIS OF CHOICE
        {
            case 1: display_sales();   // CALL TO FUCNTION TO DISPLAY SALES
                goto prev_scr;     // MOVED TO PREVIOUS SCREEN

            case 2: change_password();   // CALL TO FUNCTION TO CHANGE PASSWORD
                goto prev_scr;       // MOVED TO PREVIOUS SCREEN

            case 3: display_rlist();   // TO DISPLAY THE RATE LIST
                settextstyle (4, HORIZ_DIR, 3);
                setcolor (YELLOW);
		outtextxy(50,430," Press any key to return to the previous screen");
		getch();
		setcolor (RED);
		goto prev_scr;

	    case 4: mod_scr:
		cleardevice();

		mod_screen();   // GRAPHICAL "MODIFY" SCREEN DISPLAYED

		for(;;)
		{
		    key=getch();

		    if(key!=13)
		    {
			if(key==72)  // IF UP KEY IS PRESSED
			{
			    setcolor(YELLOW);
			    outtextxy (maxx/2-150, mod_cord, "*");
			    mod_cord-=50;

			    if(mod_cord<190)
				mod_cord=190;

			    setcolor(RED);
			    outtextxy (maxx/2-150, mod_cord, "*");
			}

			else if ( key == 80 )   // IF DOWN KEY IS PRESSED
			{
			    setcolor(YELLOW);
			    outtextxy (maxx/2-150, mod_cord, "*");
			    mod_cord+=50;

			    if(mod_cord>340)
				mod_cord=340;

			    setcolor(RED);
			    outtextxy (maxx/2-150, mod_cord, "*");
			}

		    }


		    if(key==13)    // IF ENTER KEY IS PRESSED

			break;


		}  // END OF FOR LOOP

		switch(mod_cord)
		{

		    case 190: ch_md=1;
			  break;

		    case 240: ch_md=2;
			  break;

		    case 290: ch_md=3;
			  break;

		    case 340: goto prev_scr;
		}
	}


	switch (ch_md)
	{
	    case 1: cleardevice();
		add_item();           // ADDING THE ITEM AND
		display_rlist();      // DISPLAYING THE RATE LIST
		settextstyle (4, HORIZ_DIR, 3);
		setcolor (YELLOW);
		outtextxy(50,430," Press any key to return to the previous screen");
		getch();
		setcolor (RED);
		goto mod_scr;

	    case 2: delete_item();      // TO DELETE ITEM
		settextstyle (4, HORIZ_DIR, 3);
		setcolor (YELLOW);
		outtextxy(50,430," Press any key to return to the previous screen");
		getch();
		setcolor (RED);
		goto mod_scr;

	    case 3: mod_item();     // TO MODIFY THE ITEM
		settextstyle (4, HORIZ_DIR, 3);
                setcolor (YELLOW);
                outtextxy(50,430," Press any key to return to the previous screen");
                getch();
                setcolor (RED);
                goto mod_scr;

        }

    }  // END OF OWNER MENU

    else if (strcmpi (p,pass_e)==0 )   // IF EMPLOYEE's PASSWORD MATCHES
    {
        emp:

        cleardevice();
        y_cord=190;     // COORDINATE OF THE CURSOR
        emp_screen();
        int key;

        for(;;)
        {
         key=getch();

         if(key!=13)
         {
            if(key==72)   // IF UP KEY IS PRESSED
            {
                setcolor(YELLOW);
                outtextxy (maxx/2-150, y_cord, "*");
                y_cord-=50;

                if(y_cord<190)
                    y_cord=190;

                setcolor(RED);
                outtextxy (maxx/2-150, y_cord, "*");
             }

             else if(key==80)      // IF DOWN KEY IS PRESSED
             {
                setcolor(YELLOW);
                outtextxy (maxx/2-150, y_cord, "*");
                y_cord+=50;

                if(y_cord>340)
                    y_cord=340;

                setcolor(RED);
                outtextxy (maxx/2-150, y_cord, "*");
             }

         }

         if(key==13)   // IF ENTER IS PRESSED
            break;





        }  // END OF FOR LOOP



        switch(y_cord)
        {

            case 190:  create_bill();    // FUNCTION FOR CREATNG THE BILL
                   save_sales();    // SAVING SALES IMMEDIATELY AFTER CREATING THE BILL
                   goto emp;

            case 240:  instructions_for_emp(); // INSTRUCTIONS FOR THE EMPLOYEE
                   getch();
                   goto emp;

            case 290:  cleardevice();
                   goto beg;    // 'LOG OFF' OPTION (BACK TO THE PASSWORD PROMPT)

            case 340:  exit(0);


         }


    }   // END OF EMPLOYEE MENU

    else
    {
        settextstyle (4, HORIZ_DIR, 4);
        outtextxy (190,maxy/2+80,"Wrong Password! ");
        getch();
    }

}

/*-------------------------------------------------------------------------*/
//               FUNCTION DEFINITIONS
/*-------------------------------------------------------------------------*/


void set_graph()
{
     int mode;
     int driver=DETECT;
     initgraph(&driver,&mode,"c:\\tc");
}

void gap(int x)
{
     for(int i=0;i<x;i++)
     cout<<endl;
}

void get_pass_frm_file()       // FUNCTION TO GET THE PASSWORD FROM
                   // AN ALREADY CREATED FILE
{
    int z=0;

    ifstream isfile ("ownerp.dat", ios::in | ios::binary);
    ifstream isfile1 ("empp.dat", ios::in | ios::binary);

    while (isfile)
    {
        isfile.get(pass_o[z]);
        z++;
    }

    pass_o[--z]='\0';      // ASSIGNING THE LAST CHARACHTER OF THE
                   // STRING AS NULL
    z=0;

    while (isfile1)
    {
        isfile1.get(pass_e[z]);
        z++;
    }

    pass_e[--z]='\0';

    isfile.close();
    isfile1.close();
}
/*-------------------------------------------------------------------------*/
//              GRAPHICAL SCREENS USED
/*-------------------------------------------------------------------------*/


void icon()            // ANIMATED SCREEN FOR THE INTRO
{
    setbkcolor (YELLOW);
    setcolor (GREEN);
    rectangle ( 20,20, 620,460);
    rectangle ( 22,22, 618,458);
    rectangle ( 20+4,20+4, 620-4,460-4);
    rectangle ( 22+4,22+4, 618-4,458-4);
    rectangle ( 20+6,20+6, 620-6,460-6);
    rectangle ( 22+6,22+6, 618-6,458-6);
    setfillstyle (SOLID_FILL, YELLOW);
    ellipse (295, 230, 0, 360, 70, 10);
    line (225, 230, 225, 400);
    line (365, 230, 365, 400);
    line(225,400,365,400);
    settextstyle (4, 0, 10);
    outtextxy (149, 230, "C");
    settextstyle (4, 0, 4);
    outtextxy (228, 260, "afe");
    outtextxy (228, 285, "offee");
    outtextxy (228, 310, "Day");
    setcolor (LIGHTGRAY);
    arc (359, 185, 170, 195, 100);
    arc (160, 155, 180+171, 180+195, 100);
    arc (389, 185, 170, 195, 100);
    arc (190, 155, 180+171, 180+195, 100);
    arc (419,185,170,195,100);
    arc (220,155,180+171,180+195,100);
    settextstyle (4, 0, 7);
    setcolor (RED);
    outtextxy (92, 35, "Cafe Coffee Day");
    line (92, 105, 545, 105);

    for ( int A=400; A>340; A-- )
    {
        setfillstyle (SOLID_FILL, BROWN);
        bar3d (350, 210, 345, 205, 30, 10);
        setfillstyle (SOLID_FILL, YELLOW);
        bar (340, 210, A, 180);
        delay (25);
    }

    for ( int K=210 ; K<400 ; K++ )
    {
        line (346, 210, 350, K);
        line (347, 210, 350, K);
        line (348, 210, 350, K);
        delay (12);
    }

    setcolor (GREEN);
    setfillstyle (SOLID_FILL, BROWN);

    for ( int C=400 ; C>232 ; C-- )
    {
        bar ( 226, C, 364, 400 );
        settextstyle ( 4, 0, 10);
        outtextxy (149, 230, "C");
        settextstyle ( 4, 0, 4);
        outtextxy ( 228, 260, "afe");
        outtextxy ( 228, 285, "offee");
        outtextxy ( 228, 310, "Day");
        setcolor (GREEN);
        ellipse (295, 230, 0, 360, 70, 10);
        delay (26);
    }

    setfillstyle (SOLID_FILL, YELLOW);

    for ( int B=232 ; B>210 ; B-- )
    {
        bar (345, 232, 351, B);
        ellipse (295, 230, 0, 360, 70, 10);
        delay(15);
    }

    for ( int Z=340 ; Z<400 ; Z++ )
    {
        setfillstyle (SOLID_FILL, YELLOW);
        bar (340, 210, Z, 180);
        delay (75);
    }

    delay (2000);
    closegraph ();
    set_graph();
}

void emp_screen()
{
    maxx=getmaxx();
    maxy=getmaxy();
    settextstyle (4, HORIZ_DIR, 6);
    setbkcolor (YELLOW);

    setcolor (GREEN);
    rectangle ( 20,20, 620,460);
    rectangle ( 22,22, 618,458);
    rectangle ( 20+4,20+4, 620-4,460-4);
    rectangle ( 22+4,22+4, 618-4,458-4);
    rectangle ( 20+6,20+6, 620-6,460-6);
    rectangle ( 22+6,22+6, 618-6,458-6);

    setcolor (RED);
    outtextxy (maxx/2-200, maxy/2 - 200, "Cafe Coffee Day");
    line (maxx/2-200, maxy/2-140, maxx/2+176, maxy/2-140);
    settextstyle (4, HORIZ_DIR, 4);
    outtextxy (maxx/2-130, maxy/2-50 , " Create Bill");
    outtextxy (maxx/2-130, maxy/2 , " Instructions");
    outtextxy (maxx/2-130, maxy/2+50 , " Log Off");
    outtextxy (maxx/2-130, maxy/2+100 , " Turn Off");
    outtextxy (maxx/2-150, y_cord, "*");
}

void owner_screen()
{
    maxx=getmaxx();
    maxy=getmaxy()-100;
    settextstyle (4, HORIZ_DIR, 6);
    setbkcolor (YELLOW);

    setcolor (GREEN);
    rectangle ( 20,20, 620,460);
    rectangle ( 22,22, 618,458);
    rectangle ( 20+4,20+4, 620-4,460-4);
    rectangle ( 22+4,22+4, 618-4,458-4);
    rectangle ( 20+6,20+6, 620-6,460-6);
    rectangle ( 22+6,22+6, 618-6,458-6);

    setcolor (RED);
    outtextxy (maxx/2-200, maxy/2 - 150, "Cafe Coffee Day");
    line (maxx/2-200, maxy/2-90, maxx/2+176, maxy/2-90);
    settextstyle (4, HORIZ_DIR, 4);
    outtextxy (maxx/2-130, maxy/2-50 , " View sales");
    outtextxy (maxx/2-130, maxy/2 , " Change Password");
    outtextxy (maxx/2-130, maxy/2+50 , " View Rate List");
    outtextxy (maxx/2-130, maxy/2+100 , " Modify Rate List");
    outtextxy (maxx/2-130, maxy/2+150 , " Log Off");
    outtextxy (maxx/2-130, maxy/2+200 , " Turn Off");
    outtextxy (maxx/2-150, y_cord-50, "*");
}

void intro()
{
    maxx=getmaxx();
    maxy=getmaxy();
    settextstyle (4, HORIZ_DIR, 6);
    setbkcolor (YELLOW);

    setcolor (GREEN);
    rectangle ( 20,20, 620,460);
    rectangle ( 22,22, 618,458);
    rectangle ( 20+4,20+4, 620-4,460-4);
    rectangle ( 22+4,22+4, 618-4,458-4);
    rectangle ( 20+6,20+6, 620-6,460-6);
    rectangle ( 22+6,22+6, 618-6,458-6);

    setcolor (RED);
    outtextxy (maxx/2-200, maxy/2 - 200, "Cafe Coffee Day");
    line (maxx/2-200, maxy/2-140, maxx/2+176, maxy/2-140);
    outtextxy (200, maxy/2-50 , "Password");
    rectangle (200, maxy/2 +30, 410, maxy/2+70);
}

void mod_screen()
{
    maxx=getmaxx();
    maxy=getmaxy();
    settextstyle (4, HORIZ_DIR, 6);
    setbkcolor (YELLOW);

    setcolor (GREEN);
    rectangle ( 20,20, 620,460);
    rectangle ( 22,22, 618,458);
    rectangle ( 20+4,20+4, 620-4,460-4);
    rectangle ( 22+4,22+4, 618-4,458-4);
    rectangle ( 20+6,20+6, 620-6,460-6);
    rectangle ( 22+6,22+6, 618-6,458-6);

    setcolor (RED);
    outtextxy (maxx/2-200, maxy/2 - 200, "Cafe Coffee Day");
    line (maxx/2-200, maxy/2-140, maxx/2+176, maxy/2-140);
    settextstyle (4, HORIZ_DIR, 4);
    outtextxy (maxx/2-130, maxy/2-50 , " Add Item");
    outtextxy (maxx/2-130, maxy/2 , " Delete Item");
    outtextxy (maxx/2-130, maxy/2+50 , " Modify Existing");
    outtextxy (maxx/2-130, maxy/2+100 , " Return");
    outtextxy (maxx/2-150, mod_cord, "*");
}
void instructions_for_emp()
{
    cleardevice();
    setcolor (YELLOW);
    rectangle(2,2,638,478);
    rectangle(2+2,2+2,638-2,478-2);
    setbkcolor (RED);
    settextstyle ( 7, 0, 4);
    outtextxy ( 5, 5, "Welcome, ");
    settextstyle ( 7, 0, 2);

    outtextxy ( 18, 50, "There are two access levels for this software:  ");
    outtextxy ( 18, 80, "(a) Owner (b) Employee ");
    outtextxy ( 18, 110, "You have to enter the password to log on and view the");
    outtextxy ( 18, 140, "menu. The default passwords have been assigned as");
    outtextxy ( 18, 170, "'employee' for employee and 'owner' for owner,");
    outtextxy ( 18, 200, "which can later be changed by the user of owner");
    outtextxy ( 18, 230, "account. ");
    outtextxy ( 18, 290, "EMPLOYEE MENU: It has the following options :-");
    outtextxy ( 18, 320, "1. Create bill");
    outtextxy ( 18, 350, "2. Instructions");
    outtextxy ( 18, 380, "3. Log off");
    outtextxy ( 18, 410, "4. Turn off");

    outtextxy(100,450,"(Press any key to go to the next screen)");
    getch();

    cleardevice();

    rectangle(2,2,638,478);
    rectangle(2+2,2+2,638-2,478-2);
    outtextxy ( 18, 50, "Create Bill: Enter the code of the item. Then enter the");
    outtextxy ( 18, 80, "quantity and the corresponding amount alongwith bill ");
    outtextxy ( 18, 110, "details will be displayed and added to the bill. To ");
    outtextxy ( 18, 140, "terminate the billing process enter '123' in the item ");
    outtextxy ( 18, 170, "code. If you need to view the rate list in between, ");
    outtextxy ( 18, 200, "just enter '0' in the item code. ");
    settextstyle ( 7, 0, 1);
    outtextxy ( 18, 230, "NOTE: A BILL CAN CONATIN AT MOST 23 ITEMS. FOR ADDING");
    outtextxy ( 18, 260, "MORE ITEMS, A NEW BILL HAS TO BE GENERATED");
    settextstyle ( 7, 0, 2);
    outtextxy ( 18, 320, "Log Off: To return to the log in screen and enter");
    outtextxy ( 18, 350, "password again");
    outtextxy ( 18, 410, "Turn off: To exit the software");

    getch();

    cleardevice();

    rectangle ( 20,20, 620,460);
    rectangle ( 22,22, 618,458);
    rectangle ( 20+4,20+4, 620-4,460-4);
    rectangle ( 22+4,22+4, 618-4,458-4);
    rectangle ( 20+6,20+6, 620-6,460-6);
    rectangle ( 22+6,22+6, 618-6,458-6);
    settextstyle (7, 0, 5);
    outtextxy ( 200, 40, "Made by: ");
    outtextxy ( 240, 140, "Rajat");
    outtextxy ( 280, 200, "&");
    outtextxy ( 240, 260, "Rohit");
    outtextxy ( 200, 340, "XII - B");


}

void verify_pass_screen()
{
    int x=190,y=400;
    settextstyle(4,0,4);
    int i=0;
    do
    {
	outtextxy(x,y,"Verifying");
	delay(300);
	outtextxy(x,y,"Verifying .");
	delay(300);
	outtextxy(x,y,"Verifying . .");
	delay(300);
	outtextxy(x,y,"Verifying . . .");
	delay(300);
	outtextxy(x,y,"Verifying . . . .");
	delay(300);
	setcolor(YELLOW);
	outtextxy(x,y,"Verifying . . . .");
	i++;
	setcolor(RED);
    }
    while(i<3);
}

/*-------------------------------------------------------------------------*/
//            FUNCTIONS TO ADD, DELETE, MODIFY AND DISPLAY RATE LIST
/*-------------------------------------------------------------------------*/

void add_item()    // FUNCTION TO ADD ITEM
{
    setbkcolor(RED);

    ofstream file;
    file.open ("rlist.dat" , ios::out | ios::binary | ios::app );

    item a1;
    char ans;

    do
    {
	a1.get();
	file.write ( (char*)&a1, sizeof(item) );
	cout<<"\n\nAdd more items? (y/n): ";
	cin>>ans;

    } while (ans!='n');

    file.close();

    setbkcolor(YELLOW);
}

void display_rlist()   // FUNCTION TO DISPLAY RATE LIST
{
    int count = 0;
    setbkcolor(RED);
    y=3;
    ifstream ifile ("rlist.dat" , ios::in | ios::binary);
    item a1;
    ifile.seekg(0);

    ifile.read ( (char*)&a1 , sizeof (item) );

    clrscr();
    cleardevice();

    setcolor (YELLOW);
    rectangle ( 20,20, 620,460+3+5);
    rectangle ( 22,22, 618,458+3+5);
    rectangle ( 20+4,20+4, 620-4,460-4+3+5);
    rectangle ( 22+4,22+4, 618-4,458-4+3+5);
    rectangle ( 20+6,20+6, 620-6,460-6+3+5);
    rectangle ( 22+6,22+6, 618-6,458-6+3+5);

    gotoxy(4,y);
    cout<<"Item Code";
    gotoxy(31,y);
    cout<<"Item Name";
    gotoxy(68,y);

    cout<<"Item Price";
    y=5;

    while ( !ifile.eof() )
    {
	count++;
	if (count < 24)

	     a1.display();

	else
	{
	    delay(4500);
	    clrscr();
	    cleardevice();

	    gotoxy(4,1);
	    cout<<"Item Code";
	    gotoxy(31,1);
	    cout<<"Item Name";
	    gotoxy(68,1);
	    cout<<"Item Price";
	    y=5;
	    a1.display();
	    count = 0;
	}
	ifile.read ( (char*)&a1 , sizeof (item) );
    }

    ifile.close();
    y=1;
}

void delete_item()       // FUNCTION TO DELETE AN ITEM
{

    clrscr();
    cleardevice();
    setbkcolor(RED);

    item a;
    int code1,found=0;

    ifstream ikfile("rlist.dat",ios::in|ios::binary);
    ofstream okfile("temp.dat",ios::out|ios::trunc|ios::binary);

    cout<<"Enter the code to be deleted : ";
    cin>>code1;

    ikfile.read ( (char*)&a, sizeof(a) );

    while ( !ikfile.eof() )
    {
	if ( code1 != a.ret() )

	       okfile.write ( (char*)&a, sizeof(a) );

	else
	      found=1;

	ikfile.read((char*)& a,sizeof(a));
    }

    if ( found==0 )

	  cout<<"\n\nRecord not found.\n";

    else

	  cout<<"\n\nSuccess. Record Deleted";


    okfile.close();
    ikfile.close();

    remove("rlist.dat");
    rename("temp.dat","rlist.dat");

    delay(2500);
    display_rlist();

}

void mod_item()         // FUNCTION TO MODIFY AN ITEM
{
      clrscr();
      cleardevice();
      setbkcolor(RED);

      int code,found=0;
      item a;

      cout<<"Enter the code number of item to be modified : ";
      cin>>code;

      int rec=0;

      fstream file("rlist.dat",ios::in|ios::out|ios::binary);
      file.seekg(0);

      file.read((char*)&a,sizeof(item));

      while(!file.eof())
      {
       if ( code==a.ret() )
       {
	a.modify();
	file.seekp(rec*sizeof(item),ios::beg);
	file.write((char*)&a,sizeof(item));
	found=1;
       }

       file.read((char*)&a,sizeof(item));
       rec++;
      }

      if ( found==0 )

       cout<<"\n\nItem not found !\n";

      else

       display_rlist();


      file.close();

}


void change_password()         // FUNCTION TO CHANGE THE PASSWORD
{
    clrscr();
    cleardevice();

    char C[9];

    settextstyle (4, HORIZ_DIR, 4);
    setbkcolor (YELLOW);

    cout<<"\n  Which password do you wish to change?  (owner/employee) : ";

    gets(C);

    if(strcmpi(C,"owner")==0)
    {
      clrscr();
      cleardevice();
      char comp [10];

      cout<<"\n\n  Enter old password: ";
      gets (comp);

      if ( strcmpi (comp, pass_o) == 0 )
      {
	re_enter1:

	cout<<"\n\n  Enter new password: ";
	gets(pass_o);

	if (strlen (pass_o) > 10 )
	{
	    cout<<"\n  Enter within 10 characters";
	    goto re_enter1;
	}

	ofstream afile ("ownerp.dat",ios::out | ios::trunc | ios::binary);
	afile<<pass_o;
	afile.close();
      }

      else
      {
	cout<<"\n\n  Wrong Password !\n";
	settextstyle (4, HORIZ_DIR, 3);
	setcolor (RED);
	outtextxy(50,430," Press any key to return to the previous screen");
	getch();
      }

    }

    else if ( strcmpi (C,"employee") == 0 )
    {

      clrscr();
      cleardevice();

      char compare [10];

      cout<<"\n\n  Enter old password: ";
      gets (compare);

      if ( strcmpi (compare, pass_e) == 0 )
      {
	 re_enter:

	 cout<<"\n\n  Enter new password: ";
	 gets(pass_e);

	 if (strlen (pass_e) > 10 )
	 {
	cout<<"\n  Enter within 10 characters";
	goto re_enter;
	 }

	 ofstream afile ("empp.dat",ios::out | ios::trunc | ios::binary);

	 afile<<pass_e;

	 afile.close();

      }

      else
      {
	   cout<<"\n\n  Wrong Password !\n";
	   settextstyle (4, HORIZ_DIR, 3);
	   setcolor (RED);
	   outtextxy(50,430," Press any key to return to the previous screen");
	   getch();

      }
    }

    else
    {
	 cout<<"\n  Please Enter Correctly!";
	 settextstyle (4, HORIZ_DIR, 3);
	 setcolor (RED);
	 outtextxy(50,430," Press any key to return to the previous screen");
	 getch();
    }
}

void create_bill()       // FUCNTION FOR CREATING THE BILL
{
    int emp_code;
    int i=0;
    int quantity,quant[100];  //   ARRAYS USED
    char names [20][100];     //   FOR DISPLAYING THE ITEM
    float prices [100];      //    IMMEDIATELY AFTER THE ITEM IS ADDED

    bill=0.0;
    item d;

    ifstream file;

    read_emp_code:

    for (;;)
    {

	clrscr();
	cleardevice();

	int found=0;
	int  y=10;

	gotoxy(73, 8);
	cout<<"Price";
	gotoxy(73, 9);
	cout<<"-----";
	gotoxy(48.5,8);
	cout<<"Item";
	gotoxy(48.5, 9);
	cout<<"-----";
	gotoxy(61, 8);
	cout<<"Quantity";
	gotoxy(61, 9);
	cout<<"--------";

	for ( int j=0; j<i; j++)
	{
	    gotoxy(73,y);
	    cout<<"Rs. "<<prices[j];
	    gotoxy(45.5,y);
	    cout<<names[j];
	    cout<<endl;
	    gotoxy(65, y);
	    cout<<quant[j];
	    cout<<endl;
	    y++;

	    if (y>24)
	    {
		clrscr();
		cleardevice();
		outtextxy (120, 200, "Limit of items exceeded");
		outtextxy (120, 240, "Please create another bill!!");
		outtextxy(50,430," Press any key to view the current bill");
		getch();

		goto show_bil;
	    }

	}

	settextstyle(4, HORIZ_DIR, 3);
	outtextxy(380,400,"Total Bill: Rs. ");
	char string[7];
	itoa (bill, string, 10);
	outtextxy(550,400,string);
	setbkcolor(RED);
	setcolor(YELLOW);
	settextstyle(4, HORIZ_DIR, 4);
	outtextxy(150, 20, "CAFE COFFEE DAY");
	line (0,80,640,80);
	line (350,80,350,500);
	line (350,380,680,380);
	gotoxy (4,8);
	cout<<"Enter the code of the item you wish to";
	gotoxy (4,9);
	cout<<"add to the bill: ";
	cin>>emp_code;

	if ( emp_code ==  123)
	{
	 show_bil:
	 y=10;
	 clrscr();
	 cleardevice();
	 setbkcolor(RED);
	 setcolor(YELLOW);
	 settextstyle(4, HORIZ_DIR, 4);
	 outtextxy(150, 20, "CAFE COFFEE DAY");
	 line (0,80,640,80);
	 line (180 ,390,400,390);
	 settextstyle(4, HORIZ_DIR, 3);
	 outtextxy(183,400,"Total Bill: Rs. ");
	 char string[7];
	 itoa (bill, string, 10);
	 outtextxy(353,400,string);
	 line (180 ,450,400,450);

	 gotoxy(10,8);
	 cout<<"Item";
	 gotoxy(10, 9);
	 cout<<"-----";
	 gotoxy(55, 8);
	 cout<<"Price";
	 gotoxy(55, 9);
	 cout<<"-----";
	 gotoxy (35, 8);
	 cout<<"Quantity";
	 gotoxy (35,9);
	 cout<<"--------";

	 for ( int j=0; j<i; j++)
	 {
	    gotoxy(10,y);
	    cout<<names[j];
	    gotoxy(55,y);
	    cout<<"Rs. "<<prices[j];
	    cout<<endl;
	    gotoxy(35, y);
	    cout<<quant[j];
	    cout<<endl;
	    y++;
	 }


	 getch();
	 break;
	}

	else if (emp_code == 0)
	{
	    display_rlist();
	    settextstyle (4, HORIZ_DIR, 3);
	    setcolor (YELLOW);
	    outtextxy(50,430," Press any key to return to the previous screen");
	    getch();
	    setcolor (RED);

	    goto read_emp_code;
	}


	gotoxy(4,11);
	cout<<"Quantity : ";
	cin>>quantity;

	file.open ("rlist.dat", ios::in | ios::binary);

	file.read ( (char*)&d, sizeof (d) );

	while (file)
	{
	    if ( d.ret() == emp_code)
	    {
		bill = bill +( d.ret_price()*quantity);
		found = 1;

		quant [i] = quantity;
		prices[i] = d.ret_price()*quantity;
		strcpy(names[i],d.ret_name());
		i++;
		break;
	    }
	    file.read ( (char*)&d, sizeof (d) );

	}

	if (found == 0)
	{
	    gotoxy (4,11);
	    cout<<"Item not found!";
	    getch();
	}

	file.close();


    }  // END OF FOR LOOP
}


void save_sales ()         // FUCNTION TO SAVE THE SALES
{
    sales a;

    int rec = 0, found = 0;

    date s;

    getdate (&s);


    fstream file ("sales.dat", ios::in | ios::out | ios::binary);


    file.read ( (char*)&a, sizeof (a) );

    while ( !file.eof() )
    {
	if ( a.d.da_year == s.da_year && a.d.da_mon == s.da_mon && a.d.da_day == s.da_day)
	{
	    a.sale += bill;
	    found = 1;
	    file.seekp(rec*sizeof(a),ios::beg);
	    file.write((char*)& a,sizeof(a));
	    break;
	}

	else
	{
	    file.read ( (char*)&a, sizeof (a) );
	    rec++;
	}
    }

    file.close();

    if ( found == 0 )
    {
	a.d.da_year = s.da_year;
	a.d.da_mon = s.da_mon;
	a.d.da_day = s.da_day;
	a.sale = bill;
	fstream file1 ("sales.dat", ios::out | ios::binary | ios::app);
	file1.seekp(0,ios::end);

	file1.write ( (char*)&a, sizeof (a) );
	file1.close();
    }
}

void display_sales ()         // FUNCTION TO DISPLAY THE SALES
{
    sales a;

    clrscr();
    cleardevice();

    setbkcolor(RED);

    ifstream ifile;
    ifile.open ("sales.dat", ios::in | ios::binary);

    ifile.seekg(0);
    ifile.read ( (char*)&a, sizeof (a) );

    int cursor=5;

    setcolor (YELLOW);

    rectangle (20,20,620,460);
    rectangle (18,18,622,462);

    while ( !ifile.eof() )
    {
	cursor++;
	if (cursor < 24)
	{
	     gotoxy(22,2);
	     cout<<"Date";
	     gotoxy(22,3);
	     cout<<"-----";

	     gotoxy(48,2);
	     cout<<"Sales";
	     gotoxy(48,3);

	     cout<<"-----";

	     gotoxy ( 11, cursor);
	     a.show_data();
	}

	else
	{
	    cursor=5;
	    delay(4500);
	    clrscr();
	    cleardevice();

	    gotoxy(22,2);
	    cout<<"Date";
	    gotoxy(22,3);
	    cout<<"-----";

	    gotoxy(48,2);
	    cout<<"Sales";
	    gotoxy(48,3);

	    cout<<"-----";

	    gotoxy ( 11, cursor);
	    a.show_data();
	}
	ifile.read ( (char*)&a , sizeof (sales) );
    }

    ifile.close();

    getch();
}


/*-------------------------------------------------------------------------*/
//               END OF PROGRAM CODE
/*-------------------------------------------------------------------------*/