#include <iostream.h>
#include <fstream.h>
#include <conio.h>
#include <ctype.h>
#include <process.h>
#include <string.h>
#include <stdio.h>

class TICKET
{
	public :
			void ADD(void) ;
			void ENQ(void) ;
			char *FLNO(int) ;
	private :
			void ADD_REC(char[10], char[15], char[15], int, int) ;
	protected :
			char fltno[10], from[15], to[15] ;
			int  eco, exe,sno ;
} ;

class PASSENGER
{
	public :
			void ADD_REC(int, int, char[26], char[36], int, char, char) ;
			void DEL_TIC(int) ;
			int  DEL_FL(int) ;
			void LIST(void) ;
			int  LAST_TICNO(void) ;
			int NOFSEATS(int) ;
			int  FND(int) ;
			char *NAME(int) ;
			char CLASS(int) ;
	protected :
			char Class, name[26], address[36], sex ;
			int  slno, age, ticno ;
} ;



class RESERVE : public TICKET, public PASSENGER
{
	public :
			void RESERV(void) ;
			void CANCEL(void) ;
			void DISP_TIC(void) ;
			void DEL_ALL(void) ;
} ;



void TICKET :: ADD_REC(char t_fltno[10], char t_from[15], char t_to[15], int t_eco, int t_exe)
{
	fstream file ;
	file.open("TICKET.DAT", ios::app) ;
	strcpy(fltno,t_fltno) ;
	strcpy(from,t_from) ;
	strcpy(to,t_to) ;
	eco = t_eco ;
	exe = t_exe ;
	file.write((char *) this, sizeof(TICKET)) ;
	file.close() ;
}

char *TICKET :: FLNO(int sno)
{
	fstream file ;
	file.open("TICKET.DAT", ios::in) ;
	int count=1 ;
	char flino[10];
	while (file.read((char *) this, sizeof(TICKET)))
	{
		if (sno == count)
			break ;
		count++ ;
	}
	strcpy(flino,fltno);
	file.close() ;
	return flino ;
}

void TICKET :: ADD(void)
{
	fstream file ;
	file.open("TICKET.DAT", ios::in) ;
	if (!file.fail())
		return ;
	file.close() ;
	ADD_REC("KL146","DELHI","MUMBI",1600,1700) ;
	ADD_REC("KL146","MUMBI","DELHI",1600,1700) ;
	ADD_REC("KL156","DELHI","CALCUTTA",1800,1900) ;
	ADD_REC("KL156","CALCUTTA","DELHI",1800,1900) ;
	ADD_REC("KL166","DELHI","MADRAS",2200,2300) ;
}

void TICKET :: ENQ()
{
	clrscr() ;
	fstream file ;
	file.open("TICKET.DAT", ios::in) ;
	cout<<"LIST OF THE FLIGHTS" ;
	cout<<" Sno. FLIGHT NO.    FROM          TO           ECONOMIC FAIR  EXECUTIVE FAIR";
	while (file.read((char *) this, sizeof(TICKET)))
	{     	cout<<"\t";
		cout<<sno;
		cout<<"\t";
		cout<<fltno ;
		cout<<"\t";
		cout<<from ;
		cout<<"\t";
		cout<<to ;
		cout<<"\t";
		cout<<eco ;
		cout<<"\t"<<"\t";
		cout<<exe ;
		cout<<"\n"<<" \n";
	}
	file.close() ;
}

void RESERVE :: RESERV()
{
	clrscr() ;
	ENQ() ;
	char t1[5], pclass, pname[26], paddress[36], psex, pfltno[10] ;
	int  t2, valid, page, tno, sno ;
	PASSENGER p ;
	tno = p.LAST_TICNO() + 1 ;
	do
	{
		valid = 1;
		cout <<"\nEnter Sno. of the FLIGHT : " ;
		cin>>sno;
		if (sno < 1 || sno > 5)
		{
			valid =0;
			cout <<"\nENTER CORRECTLY" ;
			getch() ;
		}
	} while (!valid) ;
	int i=1 ;
	fstream file ;
	file.open("TICKET.DAT", ios::in) ;
	while (file.read((char *) this, sizeof(TICKET)))
	{
		if (sno == i)
			break ;
		i++ ;
	}
	file.close() ;
	strcpy(pfltno,fltno) ;
	if (p.NOFSEATS(sno) >= 250)
	{
		cout <<"\nSorry! Seats are not available." ;
		getch() ;
		return ;
	}
	do
	{

		cout <<"\n By which Class you want to travel (E=Economy  X=Executive) : " ;
		 cin>>pclass;
		pclass = toupper(pclass) ;
	} while (pclass != 'E' && pclass != 'X') ;
	clrscr() ;
	cout <<"\n       RESERVATION" ;
	cout <<"\nNAME    : " ;
	gets(pname);
	cout <<"\nADDRESS : " ;
	gets(paddress);
	do
	{
		cout<<"\n";
		cout <<"SEX M/F : " ;
		cin>>psex;
		psex = toupper(psex) ;
	} while (psex != 'M' && psex != 'F') ;
	do
	{
		cout<<"\n" ; clreol() ;
		cout <<"AGE     : " ;
		cin>>page;
	} while (page<1 || page>100) ;
	clrscr() ;
	cout <<"\n            Ticket no. " <<tno ;
	cout<<"\n" <<from <<" to " <<to ;
	cout <<"\nFlight no. " <<pfltno ;
	cout <<"\nPassenger Name : " <<pname ;
	cout <<"\nAddress : " <<paddress;
	cout <<"\nSex : " <<psex ;
	cout <<"\nAge : " <<page ;
	if (pclass == 'E')
		cout <<"Total Fair: " <<eco ;
	else
		cout <<"Total Fair: " <<exe ;
	p.ADD_REC(tno,sno,pname,paddress,page,psex,pclass) ;
	getch() ;
}

void PASSENGER :: ADD_REC(int tno, int sno, char pname[26], char paddress[36], int page, char psex, char pclass)
{
	fstream file ;
	file.open("PASS.DAT", ios::app) ;
	ticno = tno ;
	slno = sno ;
	strcpy(name,pname) ;
	strcpy(address,paddress) ;
	age = page ;
	sex = psex ;
	Class = pclass ;
	file.write((char *) this, sizeof(PASSENGER)) ;
	file.close() ;
}


int PASSENGER :: LAST_TICNO(void)
{
	fstream file ;
	file.open("PASS.DAT", ios::in) ;
	int count=0 ;
	while (file.read((char *) this, sizeof(PASSENGER)))
		count = ticno ;
	file.close() ;
	return count ;
}

int PASSENGER ::NOFSEATS(int sno)
{
	fstream file ;
	file.open("PASS.DAT", ios::in) ;
	int count=0 ;
	while (file.read((char *) this, sizeof(PASSENGER)))
	{
		if (sno == slno)
			count++ ;
	}
	file.close() ;
	return count ;
}

int PASSENGER :: FND(int tno)
{
	fstream file ;
	file.open("PASS.DAT", ios::in) ;
	int found=0 ;
	while (file.read((char *) this, sizeof(PASSENGER)))
	{
		if (tno == ticno)
		{
			found = 1 ;
			break ;
		}
	}
	file.close() ;
	return found ;
}


char *PASSENGER :: NAME(int tno)
{
	fstream file ;
	file.open("PASS.DAT", ios::in) ;
	while (file.read((char *) this, sizeof(PASSENGER)))
	{
		if (tno == ticno)
			break ;
	}
	file.close() ;
	return name ;
}

char PASSENGER :: CLASS(int tno)
{
	fstream file ;
	file.open("PASS.DAT", ios::in) ;
	while (file.read((char *) this, sizeof(PASSENGER)))
	{
		if (tno == ticno)
			break ;
	}
	file.close() ;
	return Class ;
}

void PASSENGER :: LIST(void)
{
	clrscr() ;
	int  sno, valid ;
	TICKET ticket ;
	ticket.ENQ() ;
	do
	{
		valid = 1 ;
		cout <<"\nEnter Sno. of the FLIGHT for which you want to see list of passanger " ;
		cin>>sno ;
		if (sno < 1 || sno > 5)
		{
			valid = 0 ;
			clreol();
			cout <<"\nENTER CORRECTLY" ;
			getch() ;
		}
	} while (!valid) ;
	clrscr() ;
	int found=0;
	char ch ;
	cout <<"\nFlight no. " <<ticket.FLNO(sno) ;
	cout <<"\nLIST OF PASSENGER" ;
	cout<<"\nTICKET NO.    NAME                            CLASS                          ";
	fstream file ;
	file.open("PASS.DAT", ios::in) ;
	while (file.read((char *) this, sizeof(PASSENGER)))
	{
		if (sno == slno)
		{
			found = 1 ;
			cout <<"\n"<<ticno ;
			cout <<"\t"<<name;
			if (Class == 'X')
				cout	<<"\tExecutive" ;
			else
				cout <<"\tEconomic" ;
		}
	}
	if (!found)
	{
		cout <<"\n Records not found" ;
	}
	cout <<"\nPress any key to continue..." ;
	getch() ;
	file.close() ;
}


void PASSENGER :: DEL_TIC(int tno)
{
	fstream file ;
	file.open("PASS.DAT", ios::in) ;
	fstream temp ;
	temp.open("temp.dat", ios::out) ;
	while (!file.eof())
	{
		file.read((char *) this, sizeof(PASSENGER)) ;
		if (tno != ticno)
			temp.write((char *) this, sizeof(PASSENGER)) ;
	}
	file.close() ;
	temp.close() ;
	file.open("PASS.DAT", ios::out) ;
	temp.open("temp.dat", ios::in) ;
	while ( !temp.eof() )
	{
		temp.read((char *) this, sizeof(PASSENGER)) ;
		file.write((char *) this, sizeof(PASSENGER)) ;
	}
	file.close() ;
	temp.close() ;
}

int PASSENGER :: DEL_FL(int sno)
{
	fstream file ;
	file.open("PASS.DAT", ios::in) ;
	fstream temp ;
	temp.open("temp.dat", ios::out) ;
	int found = 0 ;
	while (!file.eof())
	{
		file.read((char *) this, sizeof(PASSENGER)) ;
		if (sno != slno)
			temp.write((char *) this, sizeof(PASSENGER)) ;
		else
			found = 1 ;
	}
	file.close() ;
	temp.close() ;
	file.open("PASS.DAT", ios::out) ;
	temp.open("temp.dat", ios::in) ;
	while ( !temp.eof() )
	{
		temp.read((char *) this, sizeof(PASSENGER)) ;
		file.write((char *) this, sizeof(PASSENGER)) ;
	}
	file.close() ;
	temp.close() ;
	return found ;
}

void RESERVE :: CANCEL(void)
{
	clrscr() ;
	char ch ;
	int  tno ;
	cout <<"\nEnter Ticket no. of the Passenger to cancel the Ticket " ;
	cin>>tno ;
	if (!FND(tno))
	{
		cout <<"\nRecord not found" ;
		getch() ;
		return;
	}
	clrscr() ;
	fstream file1 ;
	fstream file2;
	file1.open("PASS.DAT", ios::in) ;
	while (file1.read((char *) this, sizeof(PASSENGER)))
		if (ticno == tno)
			break ;
	int i=1 ;
	file2.open("TICKET.DAT", ios::in) ;
	while (file2.read((char *) this, sizeof(TICKET)))
	{
		if (slno == i)
			break ;
		i++ ;
	}
	PASSENGER p ;
	cout <<"\nTicket no. " <<tno ;
	cout <<"\n"<<from <<" to " <<to ;
	cout <<"\nFlight no. " <<fltno ;
	cout <<"\nPassenger Name : " <<p.NAME(tno);
	cout <<"\nAddress : " <<address ;
	cout <<"\nSex : " <<sex ;
	cout <<"\nAge : " <<age ;
	if (p.CLASS(tno) == 'E')
		cout <<"\nTotal Fair: " <<eco ;
	else
		cout <<"\nTotal Fair: " <<exe ;
		file1.close();
		file2.close();
	do
	{

		cout <<"\n"<<"\nCancel this ticket (y/n) : " ;
		cin>>ch;
		ch = toupper(ch) ;
	} while (ch != 'Y' && ch != 'N') ;
	if (ch == 'N')
		return ;
	DEL_TIC(tno) ;
	cout <<"\nTicket Cancelled" ;
	getch() ;
}

void RESERVE :: DISP_TIC(void)
{
	clrscr() ;
	char ch ;
	int  tno, valid ;
	cout <<"\nEnter Ticket no. of the Passanger to display the Ticket " ;
	cin>>tno ;
	if (!FND(tno))
	{
		cout <<"\nRecord not found" ;
		getch() ;
		return;
	}
	clrscr() ;
	PASSENGER p ;
	fstream file1,file2 ;
	file1.open("PASS.DAT", ios::in) ;
	while (file1.read((char *) this, sizeof(PASSENGER)))
		if (ticno == tno)
			break ;
	int i=1 ;
	file2.open("TICKET.DAT", ios::in) ;
	while (file2.read((char *) this, sizeof(TICKET)))
	{
		if (slno == i)
			break ;
		i++ ;
	}
	cout <<"\nTicket no. " <<tno ;
	cout <<"\n"<<from <<" to " <<to ;
	cout <<"\nFlight no. " <<fltno ;
	cout <<"\nPassenger Name : " <<p.NAME(tno);
	cout <<"\nAddress : " <<address;
	cout <<"\nSex : " <<sex;
	cout <<"\nAge : " <<age ;
	if (p.CLASS(tno) == 'E')
		cout <<"\nTotal Fair: " <<eco ;
	else
		cout <<"\nTotal Fair: " <<exe ;
		cout <<"Press any key to continue..." ;
		getch() ;
}


void RESERVE :: DEL_ALL(void)
{
	clrscr() ;
	ENQ() ;
	char t1[5] ;
	int  t2, valid, sno ;
	do
	{
		valid = 1 ;
		cout<<"\nEnter Sno. of the FLIGHT for which all passenger records to be deleted : " ;
		cin>>sno ;
		if (sno < 1 || sno > 5)
		{
			valid = 0 ;
			cout <<"\nENTER CORRECTLY" ;
			getch() ;
		}
	} while (!valid) ;
	if (!DEL_FL(sno))
		cout <<"\n\nRecords not found. Press any key to continue..." ;
	else
		cout <<"\n\nRecords deleted. Press any key to continue..." ;
	getch() ;
}


void main()
{
	PASSENGER p ;
	TICKET ticket ;
	RESERVE r ;
	ticket.ADD() ;
	int choice;
	do
	{
		clrscr() ;
		cout<<"\n\tAIR  TICKET  RESERVATION" ;
		cout<<"\n\t<<~~~~~~~~~~~~~~~~~~~~~~" ;
		cout<<"\n1: RESERVATION";
		cout<<"\n2: CANCELLATION";
		cout<<"\n3: PASSENGER RECORDS ->" ;
		cout<<"\n4: ENQUIRY" ;
		cout<<"\n5: LIST OF PASSENGERS";
		cout<<"\n6: QUIT" ;
		cout<<"\nENTER YOUR CHOICE " ;
		cin>>choice;
		clrscr() ;
		switch(choice)
		{
			case 1: r.RESERV();
				break ;
			case 2:r.CANCEL();
			       break;
			case 3:int opt;
			       do{
				   clrscr();
				   cout<<"\n\tEDIT PASSENGER RECORDS";
				   cout<<"\n\t~~~~~~~~~~~~~~~~~~~~~~~";
				   cout<<"\n 1:PASSENGER INFORMATION";
				   cout<<"\n 2:DELETE";
				   cout<<"\n 3:EXIT";
				   cout<<"\n ENTER OPTION";
				   cin>>opt;
				   if(opt==1)
				   r.DISP_TIC();
				   if(opt==2)
				   r.DEL_ALL();
				   if(opt==3)
				   exit(0);
				   else
				   cout<<"\n INVALID OPTION";
				   }while(opt!=3);
			case 4:ticket.ENQ();
			       break;
			case 5:p.LIST();
			       break ;
			case 6:break;
			default:cout<<"\n INVALID CHOICE";
	 }
	 }while(choice!=6);

}