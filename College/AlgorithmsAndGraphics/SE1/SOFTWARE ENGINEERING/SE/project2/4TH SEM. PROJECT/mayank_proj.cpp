/*first compile to create a default project in visual c++.. then alt+f7 ....under c/c++ select 
category as c++ language and enable RTTI*/

/*Made by Mayank Sehgal,Neeraj Sachdeva AND PARVEEN*/


		#include<fstream.h>
		#include<string.h>
		#include<conio.h>
		#include<typeinfo.h>
		#include<process.h>
		const unsigned long max=200000;
		enum pass_type{tstudent,tgeneral,thand,tother};
class pass
{
protected:
	char name[40];
	char fname[40];
	int age;
	char addr[50];
	char tel[20];
	int ptype;
	int route_no;
	int time;
	int fare[3][4];
	int tfare;
	unsigned long id;
	static unsigned long n;
	static pass*arr[];
public:
	virtual void input();
	char*getname()
	{
		return name;
	}
	unsigned long getid()
	{
		return id;
	}
	virtual void output();
	static void read();
	static void write();
	virtual pass_type get_type();
	static void add();
	static void display();
	static void display_name(char*);
	static void display_id(unsigned long);
	pass()
	{
		fare[0][0]=5;
		fare[1][0]=13;
		fare[2][0]=73;
		fare[0][1]=5;
		fare[1][1]=13;
		fare[2][1]=73;
		fare[0][2]=5;
		fare[1][2]=13;
		fare[2][2]=73;
		fare[0][3]=5;
		fare[1][3]=13;
		fare[2][3]=73;
		
	}
};
unsigned long pass::n;
pass*pass::arr[max];
class student:private pass
{
	char inst_name[40];
	char inst_addr[50];
	char std[40];
	char roll[20];
public:
	void input();
	void output();
};
class hand:private pass
{
	char hospital[40];
	int htype;
	char swa_no[20];
	char work_addr[50];
public:
	void input();
	void output();
};
class other:private pass
{
	char proof[80];
public:
	void input();
	void output();
};
class general:private pass
{
public:
	void input();
	void output();
};
pass_type pass::get_type()
{
	if(typeid(*this)==typeid(student))
		return tstudent;
	else if(typeid(*this)==typeid(other))
		return tother;
	else if(typeid(*this)==typeid(general))
		return tgeneral;
	else if(typeid(*this)==typeid(hand))
		return thand;
	else
	{
		cout<<"\nBad pass type.";
		exit(1);
	}
	return tstudent;
}
void pass::add()
{
	int pcat;
	cout<<"\nChoose pass category-:";
	cout<<"\n1.Student.";
	cout<<"\n2.General.";
	cout<<"\n3.Handicap.";
	cout<<"\n.4.Other category.:";
	cin>>pcat;
	if(pcat==1)
		arr[n]=new student;
	else if(pcat==2)
		arr[n]=new general;
	else if(pcat==3)
		arr[n]=new hand;
	else if(pcat==4)
		arr[n]=new other;
	else
		return;
	arr[n++]->input();
}
void pass::display_name(char*s)
{
	int flag=0;
	for(int j=0;j<n;j++)
	{
		if(arr[j]->get_type()==tstudent)
		{
			if(strcmpi(s,arr[j]->getname())==0)
			{
				cout<<"\nStudent";
				arr[j]->output();
				flag=1;
			}
		}
		if(arr[j]->get_type()==tgeneral)
		{
			if(strcmpi(s,arr[j]->getname())==0)
			{
				cout<<"\nGeneral.";
				arr[j]->output();
				flag=1;
			}
		}
		if(arr[j]->get_type()==tother)
		{
			if(strcmpi(s,arr[j]->getname())==0)
			{
				cout<<"\nOther.";
				arr[j]->output();
				flag=1;
			}	
		}
		if(arr[j]->get_type()==thand)
		{
			if(strcmpi(s,arr[j]->getname())==0)
			{
				cout<<"\nHandicapped.";
				arr[j]->output();
				flag=1;
			}
		}
	}
	if(flag==0)
		cout<<"No such record(s).";
}
void pass::display_id(unsigned long s)
{
	int flag=0;
	for(int j=0;j<n;j++)
	{
		if(arr[j]->get_type()==tstudent)
		{
			if(s==arr[j]->getid())
			{
				cout<<"\nStudent.";
				arr[j]->output();
				flag=1;
			}
		}
		if(arr[j]->get_type()==tgeneral)
		{
			if(s==arr[j]->getid())
			{
				cout<<"\nGeneral.";
				arr[j]->output();
				flag=1;
			}
		}
		if(arr[j]->get_type()==tother)
		{
			if(s==arr[j]->getid())
			{
				cout<<"\nOther.";
				arr[j]->output();
				flag=1;
			}
		}
		if(arr[j]->get_type()==thand)
		{
			if(s==arr[j]->getid())
			{
				cout<<"\nHandicapped.";
				arr[j]->output();
				flag=1;
			}
		}
	}
	if(flag==0)
		cout<<"No such record(s).";
}
void pass::display()
{
	for(int j=0;j<n;j++)
	{
		cout<<"\n_____________"<<endl;
		cout<<j+1<<")";
		switch(arr[j]->get_type())
		{
		case tstudent:cout<<"\nStudent.";
			break;
		case tother:cout<<"\nOther.";
			break;
		case tgeneral:cout<<"\nGeneral.";
			break;
		case thand:cout<<"\nHnadicapped.";
			break;
		}
		arr[j]->output();
		cout<<"\n_____________"<<endl;
	}
}
void pass::input()
{
	cout<<"\nEnter name:";
	cin>>name;
	cout<<"\nEnter father's name:";
	cin>>fname;
	cout<<"\nEnter age:";
	cin>>age;
	cout<<"\nEnter address:";
	cin>>addr;
	cout<<"\nEnter contact no.:";
	cin>>tel;
	cout<<"\n1.Single route.\n2.All route.\n3.All route deluxe.";
	cout<<"\nChoose pass type:";
	cin>>ptype;
	if(ptype==1)
	{
		cout<<"\nEnter route no.";
		cin>>route_no;
	}
	cout<<"\nEnter pass duration(in months):";
	cin>>time;
}
void student::input()
{
	pass::input();
	cout<<"\nEnter name of the institution.";
	cin>>inst_name;
	cout<<"\nEnter address of the institution.";
	cin>>inst_addr;
	cout<<"\nEnter the class:";
	cin>>std;
	cout<<"\nEnter the roll no.:";
	cin>>roll;
	tfare=(fare[ptype-1][0]*time)+10;
	cout<<"\nTotal fare to be submitted."<<tfare;
	id=n;
	cout<<"\nYour pass holder ID.:"<<id;
}
void hand::input()
{
	pass::input();
	cout<<"\nEnter name of the certificate issuing hospital.";
	cin>>hospital;
	cout<<"\n1.Blind.\n2.Deaf.\n3.Ortho.";
	cout<<"\nChoose handicap type:";
	cin>>htype;
	cout<<"\nEnter Social Welfare Association(S.W.A.) regd. no.";
	cin>>swa_no;
	cout<<"\nEnter work address:";
	cin>>work_addr;
	tfare=(fare[ptype-1][2]*time)+10;
	cout<<"\nTotal fare to be submitted."<<tfare;
	id=n;
	cout<<"\nYour pass holder ID.:"<<id;
}
void general::input()
{
	pass::input();
	tfare=(fare[ptype-1][1]*time)+10;
	cout<<"\nTotal fare to be submitted."<<tfare;
	id=n;
	cout<<"\nYour pass holder ID.:"<<id;
}
void other::input()
{
	pass::input();
	cout<<"Enter proof details.:";
	tfare=(fare[ptype-1][3]*time)+10;
	cout<<"\nTotal fare to be submitted."<<tfare;
	id=n;
	cout<<"\nYour pass holder ID.:"<<id;
}
void pass::output()
{
	cout<<"\nName:"<<name;
	cout<<"\nFather's name:"<<fname;
	cout<<"\nAge:"<<age;
	cout<<"\nAddress:"<<addr;
	cout<<"\nContact no."<<tel;
	cout<<"\nPass type no.:"<<ptype;
	cout<<"\nTotal fare submitted:"<<tfare;
	cout<<"\nPass holder ID:"<<id;
}
void student::output()
{
	pass::output();
	cout<<"\nInstitute:"<<inst_name<<endl<<inst_addr;
	cout<<"\nClass:"<<std;
	cout<<"\nRoll no."<<roll;
}
void hand::output()
{
	pass::output();
	cout<<"\nCertificate issuing hospital:"<<hospital;
	cout<<"\nType of handicap"<<htype;
	cout<<"\nS.W.A. Regd. no."<<swa_no;
	cout<<"\nWork address."<<work_addr;
}
void general::output()
{
	pass::output();
}
void other::output()
{
	pass::output();
	cout<<"\nProof details:"<<proof;
}
void pass::write()
{
	int size;
	ofstream ouf;
	pass_type passtype;
	ouf.open("pass.dat",ios::trunc|ios::binary);
	if(!ouf)
	{
		cout<<"\nCannot open file.";
		return;
	}
	for(int j=0;j<n;j++)
	{
		passtype=arr[j]->get_type();
		ouf.write((char*)&(passtype),sizeof(passtype));
		if(passtype==tstudent)
			size=sizeof(student);
		if(passtype==tother)
			size=sizeof(other);
		if(passtype==tgeneral)
			size=sizeof(general);
		if(passtype==thand)
			size=sizeof(hand);
		ouf.write((char*)(arr[j]),size);
		if(!ouf)
		{
			cout<<"\nCannot write file.";
			return;;
		}
	}
}
void pass::read()
{
	int size;
	ifstream inf;
	pass_type passtype;
	inf.open("pass.dat",ios::binary);
	if(!inf)
	{
		cout<<"\nCannot open file.";
		return;
	}
	n=0;
	while(true)
	{
		inf.read((char*)&passtype,sizeof(passtype));
		if(inf.eof())
			break;
		if(!inf)
		{
			cout<<"\nCannot read from file.";
			return;
		}
		if(passtype==tstudent)
		{
			arr[n]=new student;
			size=sizeof(student);
		}
		if(passtype==tother)
		{
			arr[n]=new other;
			size=sizeof(other);
		}
		if(passtype==tgeneral)
		{
			arr[n]=new general;
			size=sizeof(general);
		}
		if(passtype==thand)
		{
			arr[n]=new hand;
			size=sizeof(hand);
		}
		inf.read((char*)(arr[n]),size);
		if(!inf)
		{
			cout<<"\nCannot read file.";
			return;;
		}
		n++;
	}
}
void main()
{
	pass p;
	char name[40];
	unsigned long id;
	int ch;
	do
	{
		cout<<"\n\n\n\nEnter your choice.";
		cout<<"\n1.Make a pass.";
		cout<<"\n2.Search using name.";
		cout<<"\n3.Search using ID.";
		cout<<"\n4.Display all records.";
		cout<<"\nPress any other key to exit.";
		cin>>ch;
		switch(ch)
		{
		case 1:p.add();
			p.write();
			getch();
			break;
		case 2:cout<<"\nEnter name to be searched:";
			cin>>name;
			p.read();
			p.display_name(name);
			getch();
			break;
		case 3:cout<<"\nEnter ID to be searched.:";
			cin>>id;
			p.read();
			p.display_id(id);
			getch();
			break;
		case 4:p.read();
			p.display();
			getch();
		default:break;
		}
	}while(ch>=1&&ch<=4);
}