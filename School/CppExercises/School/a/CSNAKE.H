//This header file stores class SNAKE that is used in Menu
class SNAKE
	{
	void *bbody[2],*bmove[4],*bhead[4],*btail[4];
public:
	int x[50],y[50],direction[50],color,fillcolor,size;	//dir-0=up,1=left,2=down,3=right
	int style;			//0-classic,1-new
	int cell_size;
	int offsetx,offsety;		//topx,topy (same thing:-)
//member functions
	~SNAKE();			//->DESTRUCTOR
	void initialize(int,int,int,int,int,int,int);
	void move();
	void display();
	void reverse();
	int getleftdir(int dir);
	int gettopdir(int dir);
	int setdirection(int dir);
	int check_bite_itself();
	int out_of_boundary(int,int);
	int isat(int,int);
	};

void SNAKE::initialize(int u_style,int u_color,int u_fillcolor,int u_size,int u_offsetx,int u_offsety,int u_cell_size)
	{
	style=u_style;
	color=u_color;
	fillcolor=u_fillcolor;
	size=u_size;
	cell_size=u_cell_size;
	offsetx=u_offsetx;
	offsety=u_offsety;

	if(style==1)
		{
		int rb=cell_size-1,pt1=cell_size/4,pt2=cell_size*3/4; //rb=right-bottom
		int center=rb/2;
		setcolor(color);
		setfillstyle(1,fillcolor);
		setviewport(0,0,rb,rb,1);

		//Body1 ||
		rectangle(pt1,-1,pt2,rb+1);
		floodfill(center,center,getcolor());
		bbody[0]=malloc(imagesize(0,0,rb,rb));
		getimage(0,0,rb,rb,bbody[0]);
		clearviewport();

		//Body2 ==
		rectangle(-1,pt1,rb+1,pt2);
		floodfill(center,center,getcolor());
		bbody[1]=malloc(imagesize(0,0,rb,rb));
		getimage(0,0,rb,rb,bbody[1]);
		clearviewport();

		//move1 |~
		circle(rb+1,rb+1,pt2);
		circle(rb+1,rb+1,pt1);
		floodfill(center+2,center+1,getcolor());
		bmove[0]=malloc(imagesize(0,0,rb,rb));
		getimage(0,0,rb,rb,bmove[0]);
		clearviewport();

		//move2 ~|
		circle(0,rb+1,pt2);
		circle(0,rb+1,pt1);
		floodfill(center-1,center+1,getcolor());
		bmove[1]=malloc(imagesize(0,0,rb,rb));
		getimage(0,0,rb,rb,bmove[1]);
		clearviewport();

		//move3 _|
		circle(0,0,pt2);
		circle(0,0,pt1);
		floodfill(center-1,center-1,getcolor());
		bmove[2]=malloc(imagesize(0,0,rb,rb));
		getimage(0,0,rb,rb,bmove[2]);
		clearviewport();

		//move4 |_
		circle(rb+1,0,pt2);
		circle(rb+1,0,pt1);
		floodfill(center+1,center-1,getcolor());
		bmove[3]=malloc(imagesize(0,0,rb,rb));
		getimage(0,0,rb,rb,bmove[3]);
		clearviewport();

		//head 1	(~)
		sector(cell_size/2,cell_size,0,180,cell_size/4,cell_size/2);
		putpixel(cell_size/2+2,cell_size/2+4,0);
		putpixel(cell_size/2-2,cell_size/2+4,0);
		bhead[0]=malloc(imagesize(0,0,rb,rb));
		getimage(0,0,rb,rb,bhead[0]);
		clearviewport();

		//head 2	==*
		fillellipse(0,cell_size/2,cell_size/2,cell_size/4);
		putpixel(cell_size/2-4,cell_size/2+2,0);
		putpixel(cell_size/2-4,cell_size/2-2,0);
		bhead[1]=malloc(imagesize(0,0,rb,rb));
		getimage(0,0,rb,rb,bhead[1]);
		clearviewport();

		//head 3	(_)
		fillellipse(cell_size/2,0,cell_size/4,cell_size/2);
		putpixel(cell_size/2+2,cell_size/2-4,0);
		putpixel(cell_size/2-2,cell_size/2-4,0);
		bhead[2]=malloc(imagesize(0,0,rb,rb));
		getimage(0,0,rb,rb,bhead[2]);
		clearviewport();

		//head 4	*==
		sector(cell_size,cell_size/2,90,270,cell_size/2,cell_size/4);
		putpixel(cell_size/2+4,cell_size/2+2,0);
		putpixel(cell_size/2+4,cell_size/2-2,0);
		bhead[3]=malloc(imagesize(0,0,rb,rb));
		getimage(0,0,rb,rb,bhead[3]);
		clearviewport();

		//tail1		\/
		fillellipse(cell_size/2,0,cell_size/4,cell_size);
		btail[0]=malloc(imagesize(0,0,rb,rb));
		getimage(0,0,rb,rb,btail[0]);
		clearviewport();

		//tail2		<
		fillellipse(cell_size,cell_size/2,cell_size,cell_size/4);
		btail[1]=malloc(imagesize(0,0,rb,rb));
		getimage(0,0,rb,rb,btail[1]);
		clearviewport();

		//tail3		/\
		fillellipse(cell_size/2,cell_size,cell_size/4,cell_size);
		btail[2]=malloc(imagesize(0,0,rb,rb));
		getimage(0,0,rb,rb,btail[2]);
		clearviewport();

		//tail4		>
		fillellipse(0,cell_size/2,cell_size,cell_size/4);
		btail[3]=malloc(imagesize(0,0,rb,rb));
		getimage(0,0,rb,rb,btail[3]);
		clearviewport();

		setviewport(0,0,639,479,1);
		}
	for(int i=0;i<size;i++)
		{
		x[i]=0;
		y[i]=size-i-1;
		direction[i]=2;
		move();
//		display();
		}
	}

SNAKE::~SNAKE()			//Destructor
	{
	if(style==1)
		{
		for(int i=0;i<4;i++)
			{
			if(i<2)
				free(bbody[i]);
			free(bmove[i]);
			free(bhead[i]);
			free(btail[i]);
			}
		}
	}

void SNAKE::move()
	{
	for(int i=size+1;i>0;i--)
		{
		x[i]=x[i-1];
		y[i]=y[i-1];
		}

	if(direction[0]==0) y[0]--;
	else if(direction[0]==1) x[0]++;
	else if(direction[0]==2) y[0]++;
	else if(direction[0]==3) x[0]--;
	}

void SNAKE::display()
	{
	//To delete the last cell
		{
		setviewport(offsetx+cell_size*x[size],offsety+cell_size*y[size],offsetx+cell_size*(x[size]+1),offsety+cell_size*(y[size]+1),1);
		clearviewport();
		setviewport(0,0,639,479,1);
		}
	int cx,cy;

	if(style==0)		//classic
		{
		cx=offsetx+cell_size*x[0]+cell_size/2;
		cy=offsety+cell_size*y[0]+cell_size/2;

		setcolor(color);
		circle(cx,cy,cell_size/2-1);
		setfillstyle(1,fillcolor);
		floodfill(cx,cy,color);
		}
	else if(style==1)
		{
		cx=offsetx+cell_size*x[size-1];
		cy=offsety+cell_size*y[size-1];
		putimage(cx,cy,btail[direction[size-2]],COPY_PUT);

		cx=offsetx+cell_size*x[0];
		cy=offsety+cell_size*y[0];
		putimage(cx,cy,bhead[direction[0]],COPY_PUT);

		cx=offsetx+cell_size*x[1];
		cy=offsety+cell_size*y[1];

		if((direction[1]==0 && direction[0]==1) || (direction[1]==3 & direction[0]==2))
			putimage(cx,cy,bmove[0],COPY_PUT);
		else if((direction[1]==0 && direction[0]==3) || (direction[1]==1 && direction[0]==2))
			putimage(cx,cy,bmove[1],COPY_PUT);
		else if((direction[1]==1 && direction[0]==0) || (direction[1]==2 && direction[0]==3))
			putimage(cx,cy,bmove[2],COPY_PUT);
		else if((direction[1]==2 && direction[0]==1) || (direction[1]==3 && direction[0]==0))
			putimage(cx,cy,bmove[3],COPY_PUT);
		else if(getleftdir(direction[1])!=0 && getleftdir(direction[0])!=0)
			putimage(cx,cy,bbody[1],COPY_PUT);
		else    putimage(cx,cy,bbody[0],COPY_PUT);
		}

	//no further use can be changed
	for(int i=size;i>0;i--)
		direction[i]=direction[i-1];
	}

void SNAKE::reverse()
	{
	for(int i=0;i<size;i++)
		{
		x[i]=x[i+1];
		y[i]=y[i+1];
		direction[i]=direction[i+1];
		}
	}

int SNAKE::getleftdir(int dir)
	{
	if(dir==1) return 1;
	else if(dir==3) return -1;
	else return 0;
	}
int SNAKE::gettopdir(int dir)
	{
	if(dir==0) return -1;
	else if(dir==2) return 1;
	else return 0;
	}
int SNAKE::setdirection(int dir)
	{
	int moved=1;
	if(dir==1 && direction[1]!=3) direction[0]=1;
	else if(dir==2 && direction[1]!=0) direction[0]=2;
	else if(dir==3 && direction[1]!=1) direction[0]=3;
	else if(dir==0 && direction[1]!=2) direction[0]=0;
	else moved=0;

	return moved;
	}

int SNAKE::check_bite_itself()
	{
	for(int i=1;i<size;i++)
		if(x[0]==x[i] && y[0]==y[i])
			return 1;
	return 0;
	}

int SNAKE::out_of_boundary(int max_row,int max_column)
	{
	if(x[0]<0 || y[0]<0 || x[0]>=max_row || y[0]>=max_column)
		return 1;
	return 0;	//no need for else
	}

int SNAKE::isat(int row,int column)
	{
	for(int i=0;i<size;i++)
		if(x[i]==row && y[i]==column)
			return 1;
	return 0;
	}
/************************************************************/
/************************************************************/