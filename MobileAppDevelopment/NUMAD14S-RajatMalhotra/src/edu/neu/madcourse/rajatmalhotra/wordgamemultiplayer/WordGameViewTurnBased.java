package edu.neu.madcourse.rajatmalhotra.wordgamemultiplayer;

import java.util.ArrayList;

import edu.neu.madcourse.rajatmalhotra.MyProperties;
import edu.neu.madcourse.rajatmalhotra.R;
import edu.neu.madcourse.rajatmalhotra.dictionary.DictionaryMusic;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;

import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class WordGameViewTurnBased extends View{
	
	/*private static final String TIMER = "timer"; 
	private static final String SCORE = "score";*/
	private static final int ID = 46; 

	//CountDownTimer c;
	Vibrator v;
	
	Paint bgColor = new Paint();
	Paint rectColor = new Paint();
	Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
	Paint buttonPaint = new Paint();
	Paint buttonPaintOther = new Paint();
	String exitString = "EXIT";
	ArrayList<CoordinatesTP> indexesSelected = new ArrayList<CoordinatesTP>();

	boolean found = false;
	Paint selected = new Paint();

	float viewHeight = getHeight();
	float viewWidth = getWidth();
	float rectTop = (float)(viewHeight * 0.2);
	float rectBottom = (float)(viewHeight * 0.7);
	float rectHeight = rectBottom - rectTop;
	float rectWidth = getWidth();

	float height =  rectHeight / 5; // height of one tile
	float width = rectWidth / 7;    // width of one tile

	int selectedX;
	int selectedY;

	String word = "";
	private int selX;       // X index of selection
	private int selY;       // Y index of selection
	ArrayList<Rect> selRects = new ArrayList<Rect>();
	boolean touched = false;
	private final WordGameTurnBased wordgame;

	int exitButtonLeft = (int)(viewWidth * 0.55);
	int exitButtonRight = (int)(viewWidth * 0.9);
	int exitButtonTop = (int)(viewHeight * 0.75);
	int exitButtonBottom = (int)(viewHeight * 0.85);

	int hintButtonLeft = (int)(viewWidth * 0.1);
	int hintButtonRight = (int)(viewWidth * 0.45);
	int hintButtonTop = (int)(viewHeight * 0.75);
	int hintButtonBottom = (int)(viewHeight * 0.85);

	//int score = 0;
	int scoreLeft = (int)(viewWidth * 0.1);
	int scoreRight = (int)(viewWidth * 0.45);
	int scoreTop = (int)(viewHeight * 0.05);
	int scoreBottom = (int)(viewHeight * 0.15);

	//long currentTime;
	
	//int movesPlayed = 0;
	
	//final int TOTAL_MOVES = 5;

	
	int mlLeft = (int)(viewWidth * 0.55);
	int mlRight = (int)(viewWidth * 0.9);
	int mlTop = (int)(viewHeight * 0.05);
	int mlBottom = (int)(viewHeight * 0.15);
	
	/*
	int timer;
	int timerLeft = (int)(viewWidth * 0.55);
	int timerRight = (int)(viewWidth * 0.9);
	int timerTop = (int)(viewHeight * 0.05);
	int timerBottom = (int)(viewHeight * 0.15);
	*/
	
	//int opponnentScore = 0;
	int opsLeft = (int)(viewWidth * 0.25);
	int opsRight = (int)(viewWidth * 0.75);
	int opsTop = (int)(viewHeight * 0.89);
	int opsBottom = (int)(viewHeight * 0.95);


	public WordGameViewTurnBased(Context context) 
	{
		super(context);
		
		this.wordgame = (WordGameTurnBased) context;
		setFocusable(true);
		setFocusableInTouchMode(true);
		setBackgroundColor(getResources().getColor(R.color.white));

		v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

		setId(ID); 
		invalidate();
	
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		viewWidth = w;
		viewHeight = h;
		rectTop = (int)(h * 0.2);
		rectBottom = (int)(h * 0.7);
		rectHeight = rectBottom - rectTop;
		rectWidth = getWidth();
		height =  rectHeight / 5; // height of one tile
		width = rectWidth / 7;    // width of one tile

		exitButtonLeft = (int)(viewWidth * 0.55);
		exitButtonRight = (int)(viewWidth * 0.9);
		exitButtonTop = (int)(viewHeight * 0.75);
		exitButtonBottom = (int)(viewHeight * 0.85);

		hintButtonLeft = (int)(viewWidth * 0.1);
		hintButtonRight = (int)(viewWidth * 0.45);
		hintButtonTop = (int)(viewHeight * 0.75);
		hintButtonBottom = (int)(viewHeight * 0.85);

		scoreLeft = (int)(viewWidth * 0.1);
		scoreRight = (int)(viewWidth * 0.45);
		scoreTop = (int)(viewHeight * 0.05);
		scoreBottom = (int)(viewHeight * 0.15);

		/*timerLeft = (int)(viewWidth * 0.55);
		timerRight = (int)(viewWidth * 0.9);
		timerTop = (int)(viewHeight * 0.05);
		timerBottom = (int)(viewHeight * 0.15);*/
		
		mlLeft = (int)(viewWidth * 0.55);
		mlRight = (int)(viewWidth * 0.9);
		mlTop = (int)(viewHeight * 0.05);
		mlBottom = (int)(viewHeight * 0.15);
		
		opsLeft = (int)(viewWidth * 0.25);
		opsRight = (int)(viewWidth * 0.75);
		opsTop = (int)(viewHeight * 0.89);
		opsBottom = (int)(viewHeight * 0.95);

		super.onSizeChanged(w, h, oldw, oldh);
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		
		bgColor.setColor(getResources().getColor(
				R.color.black));

		rectColor.setColor(getResources().getColor(R.color.background_main));
		canvas.drawRect(0, rectTop, getWidth(), rectBottom, rectColor);

		buttonPaint.setColor(getResources().getColor(R.color.orange));
		buttonPaint.setStyle(Paint.Style.FILL);
		
		buttonPaintOther.setColor(getResources().getColor(R.color.color_button_clickable));
		buttonPaintOther.setStyle(Paint.Style.FILL);
		
		

		canvas.drawRect(exitButtonLeft, exitButtonTop, exitButtonRight, exitButtonBottom, buttonPaintOther);
		canvas.drawRect(hintButtonLeft, hintButtonTop, hintButtonRight, hintButtonBottom, buttonPaintOther);
		canvas.drawRect(scoreLeft, scoreTop, scoreRight, scoreBottom, buttonPaint);
		canvas.drawRect(mlLeft, mlTop, mlRight, mlBottom, buttonPaint);
		canvas.drawRect(opsLeft, opsTop, opsRight, opsBottom, buttonPaint);
		
		
		buttonPaint.setStyle(Paint.Style.STROKE);
		buttonPaint.setColor(getResources().getColor(
				R.color.black));
		
		buttonPaintOther.setStyle(Paint.Style.STROKE);
		buttonPaintOther.setColor(getResources().getColor(
				R.color.black));
		
		canvas.drawRect(exitButtonLeft, exitButtonTop, exitButtonRight, exitButtonBottom, buttonPaintOther);
		canvas.drawRect(hintButtonLeft, hintButtonTop, hintButtonRight, hintButtonBottom, buttonPaintOther);
		canvas.drawRect(scoreLeft, scoreTop, scoreRight, scoreBottom, buttonPaint);
		canvas.drawRect(mlLeft, mlTop, mlRight, mlBottom, buttonPaint);
		canvas.drawRect(opsLeft, opsTop, opsRight, opsBottom, buttonPaint);

		// Draw the major grid lines

		for (int i=0; i < 6; i++)
		{
			canvas.drawLine(0, rectTop + (i * height), getWidth(), rectTop + (i * height), bgColor);
		}
		for (int i=0; i < 7; i++)
		{
			canvas.drawLine((i * width), rectTop , (i*width), rectBottom, bgColor); 
		}

		// Draw the numbers...
		// Define color and style for numbers

		foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(height * 0.75f);
		foreground.setTextScaleX(width / height);
		foreground.setTextAlign(Paint.Align.CENTER);

		// Draw the number in the center of the tile
		FontMetrics fm = foreground.getFontMetrics();
		// Centering in X: use alignment (and X at midpoint)
		float x = width / 2;
		// Centering in Y: measure ascent/descent first
		float y = height / 2 - (fm.ascent + fm.descent) / 2;
		for (int i = 0; i < 5; i++) 
		{
			for (int j = 0; j < 7; j++) 
			{
				canvas.drawText(this.wordgame.getTileString(i, j), j
						* width + x, (rectTop + (i * height) + y), foreground);
			}
		}


		foreground.setTextSize((exitButtonBottom - exitButtonTop) * 0.35f);
		foreground.setTextScaleX(((exitButtonRight - exitButtonLeft) / (exitButtonBottom - exitButtonTop)) * (float)0.6);
		fm = foreground.getFontMetrics();
		x = (exitButtonRight - exitButtonLeft)/2;
		y = ((exitButtonBottom - exitButtonTop)/2) - (fm.ascent + fm.descent) / 2;

		canvas.drawText(exitString, (exitButtonLeft+x), 
				(exitButtonTop + y), foreground);

		foreground.setTextSize((hintButtonBottom - hintButtonTop) * 0.35f);
		foreground.setTextScaleX(((hintButtonRight - hintButtonLeft) / (hintButtonBottom - hintButtonTop)) * (float)0.5);
		fm = foreground.getFontMetrics();
		x = (hintButtonRight - hintButtonLeft)/2;
		y = ((hintButtonBottom - hintButtonTop)/2) - (fm.ascent + fm.descent) / 2;

		canvas.drawText("HINT", (hintButtonLeft+x), 
				(hintButtonTop + y), foreground);

		foreground.setTextSize((scoreBottom - scoreTop) * 0.35f);
		foreground.setTextScaleX(((scoreRight - scoreLeft) / (scoreBottom - scoreTop)) * (float)0.4);
		fm = foreground.getFontMetrics();
		x = (scoreRight - scoreLeft)/2;
		y = ((scoreBottom - scoreTop)/2) - (fm.ascent + fm.descent) / 2;

		canvas.drawText(("SCORE: " + Integer.toString(MyProperties.getInstance().getMyScoreTB())) , (scoreLeft+x), 
				(scoreTop + y), foreground);
		
		
		foreground.setTextSize((opsBottom - opsTop) * 0.35f);
		foreground.setTextScaleX(((opsRight - opsLeft) / (opsBottom - opsTop)) * (float)0.2);
		fm = foreground.getFontMetrics();
		x = (opsRight - opsLeft)/2;
		y = ((opsBottom - opsTop)/2) - (fm.ascent + fm.descent) / 2;

		canvas.drawText(("OPPONENT SCORE: " + MyProperties.getInstance().getOpponentScoreTB()), (opsLeft+x), 
				(opsTop + y), foreground);
		

		/*if (timer <= 5)
		{
			foreground.setColor(getResources().getColor(R.color.red));
		}
		*/
		foreground.setTextSize((mlBottom - mlTop) * 0.35f);
		foreground.setTextScaleX(((mlRight - mlLeft) / (mlBottom - mlTop)) * (float)0.32);
		fm = foreground.getFontMetrics();
		x = (mlRight - mlLeft)/2;
		y = ((mlBottom - mlTop)/2) - (fm.ascent + fm.descent) / 2;

		canvas.drawText(("MOVES LEFT: " + Integer.toString(MyProperties.getInstance().getMovesLeft())) , (mlLeft+x), 
				(mlTop + y), foreground);
		
		
		
		

		
		if (touched == true)
		{
			if (found == false || MyProperties.getInstance().getHintModeEnabledTB() == false)
			{
				selected.setColor(getResources().getColor(R.color.puzzle_selected));
				for (Rect r : selRects)
				{
					canvas.drawRect(r, selected);
				}
			}
			else
			{
				if (MyProperties.getInstance().getHintModeEnabledTB() == true)
				{
					selected.setColor(getResources().getColor(R.color.green));
					selected.setAlpha(50);
					for (Rect r : selRects)
					{
						canvas.drawRect(r, selected);
					}
				}
			}
		}
		invalidate();
	}
	
	private Rect getRect(int x, int y)
	{
		Rect r = new Rect();
		r.set((int) (x * width), 
				(int) (y * height), 
				(int) (x * width + width), 
				(int) (y * height + height));
		
		return r;
	}

	private Rect select(int x, int y)
	{
		selX = Math.min(Math.max(x, 0), 6);
		selY = Math.min(Math.max(y, 2), 6);
		return getRect(selX, selY);
	}	

	private void exitClick()
	{
		
		this.wordgame.finish();
	}

	private void hintClick()
	{
		
		if (MyProperties.getInstance().getHintModeEnabledTB() ==  false)
		{
			Toast.makeText(wordgame, "Hint Mode enabled", 
					Toast.LENGTH_SHORT).show();
			MyProperties.getInstance().setHintModeEnabledTB(true);
		}
		else
		{
			Toast.makeText(wordgame, "Hint Mode disabled", 
					Toast.LENGTH_SHORT).show();
			MyProperties.getInstance().setHintModeEnabledTB(false);
		}
		
		
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		switch (event.getAction()) 
		{
		case MotionEvent.ACTION_DOWN:
		{
			float x = event.getX();
			float y = event.getY();
			if (x >= exitButtonLeft && x <= exitButtonRight && y >=exitButtonTop && y<= exitButtonBottom)
			{
				v.vibrate(85);
				exitClick();
			}
	
			if (x >= hintButtonLeft && x <= hintButtonRight && y >= hintButtonTop && y<= hintButtonBottom)
			{
				hintClick();
				invalidate();
				v.vibrate(85);
			}
			break;
		}

		case MotionEvent.ACTION_UP:
		{
			selectedX = 0;
			selectedY = 0;

			if (found == true)
			{
				DictionaryMusic.play(wordgame, R.raw.word_found);
				int score = MyProperties.getInstance().getMyScoreTB();
				score = score + (word.length() * 10);
				MyProperties.getInstance().setMyScoreTB(score);
				wordgame.indexesSelected = this.indexesSelected;
				
				Toast.makeText(wordgame, "Word detected. Good Job!", 
						Toast.LENGTH_SHORT).show();
				
				MyProperties.getInstance().setMovesLeft(MyProperties.getInstance().getMovesLeft() - 1);
				if (MyProperties.getInstance().getMovesLeft() <= 0)
				{
					wordgame.movesUp();
				}
				wordgame.changeLetters();
				invalidate();
			}
			word = "";
			selRects.clear();
			indexesSelected.clear();
			found = false;
			invalidate();
			break;
		}

		case MotionEvent.ACTION_MOVE:
		{
			if (event.getY() >= rectTop && event.getY() <= rectBottom)
			{
				touched = true;
				int xIndex = (int) (event.getX() / width);
				int yIndex = (int) (event.getY() / height);

				if (!(selectedX == xIndex && selectedY == yIndex)) // diagonal check
				{
					if ((xIndex == (selectedX + 1) && (yIndex == selectedY + 1))
							|| (xIndex == (selectedX - 1) && (yIndex == selectedY - 1))	
							|| (xIndex == (selectedX + 1) && (yIndex == selectedY - 1))
							|| (xIndex == (selectedX - 1) && (yIndex == selectedY + 1)))
					{
						Toast.makeText(wordgame, "Sorry, You cannot make words diagonally!", 
								Toast.LENGTH_SHORT).show();
						selRects.clear();
						indexesSelected.clear();
					}

					else if (!selRects.contains(select(xIndex, yIndex)))
					{
						indexesSelected.add(new CoordinatesTP(yIndex-2, xIndex));
						selRects.add(select(xIndex, yIndex));
						v.vibrate(85);
					}

					else
					{}
					CharSequence text =  wordgame.getTileString(selY - 2, selX);
					word = word.concat(text.toString()); 
					if (word.length() >= 2 && (wordgame.isWordFound(word)))
					{
						found = true;
						
					}
					else
					{
						found = false;
					}
				}
				selectedX = xIndex;
				selectedY = yIndex;
				invalidate();
				break;
			}
		 }
		}
		return true;
	}

	
}
