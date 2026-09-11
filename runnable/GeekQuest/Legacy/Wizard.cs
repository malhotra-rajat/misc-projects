using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;



namespace CreatingA2DSprite
{
    class Wizard:Sprite
    {

        public static Vector2 Position = new Vector2(0, 0);
       // public static bool jump_stop = false;

        const string WIZARD_ASSETNAME = @"images/Geek";
        const int START_POSITION_X = 125;
        const int START_POSITION_Y = 462;
        const int WIZARD_SPEED = 160;
        const int MOVE_UP = -1;
        const int MOVE_DOWN = 1;
        const int MOVE_LEFT = -1;
        const int MOVE_RIGHT = 1;
        const int HOW_MUCH_JUMP = 155; // jump to this height


        int k = 0, s = 100,m = 0;
        enum State
        {
         Walking,
         Jumping,
        Falling,


      }

        State mCurrentState = State.Walking;
        Vector2 mStartingPosition = Vector2.Zero;
        Vector2 mDirection = Vector2.Zero;
        Vector2 mSpeed = Vector2.Zero;
        KeyboardState mPreviousKeyboardState;


        private Texture2D mSpriteTexture;
        private float scale = 1.0f;
        public static Rectangle size;

        public bool isFacingLeft = false;

        public float Scale
        {
            get
            {
                return scale;
            }
            set
            {
                scale = value;
                size = new Rectangle(0, 0, (int)((mSpriteTexture.Width) * scale), (int)((mSpriteTexture.Height) * scale));
            }
        }
        Rectangle source;

        public Rectangle Source
        {

            get { return source; }

            set
            {

                source = value;

                size = new Rectangle(0, 0, (int)(source.Width * scale), (int)(source.Height * scale));

            }

        }


        //firball var
        List<Fireball> mFireballs = new List<Fireball>();
        ContentManager mContentManager;
        //end fireball var

        public void LoadContent(ContentManager theContentManager)
        {

            mContentManager = theContentManager; //for fireball

           foreach (Fireball aFireball in mFireballs)
           {
               aFireball.LoadContent(theContentManager, @"images/Fireball");
            }

            Position = new Vector2(START_POSITION_X, START_POSITION_Y);

            source = new Rectangle(0, 0, 100, Source.Height);
            scale = 0.45f;



            mSpriteTexture = theContentManager.Load<Texture2D>(@"images/Geek");


            source = new Rectangle(0, 0, (int)(mSpriteTexture.Width), (int)(mSpriteTexture.Height));
            size = new Rectangle(0, 0, (int)(mSpriteTexture.Width * Scale), (int)(mSpriteTexture.Height * Scale));



        }

        public void Update(GameTime theGameTime)
        {

            KeyboardState aCurrentKeyboardState = Keyboard.GetState();





            UpdateMovement(aCurrentKeyboardState);

            UpdateJump(aCurrentKeyboardState);

            UpdateFireball(theGameTime, aCurrentKeyboardState);

            Position += mDirection * mSpeed * (float)theGameTime.ElapsedGameTime.TotalSeconds;

            if (Position.X > 740)
            {
                Position.X = 740;
            }
            if (Position.X < 0)
            {
                Position.X = 0;
            }

            mPreviousKeyboardState = aCurrentKeyboardState;



        }

        private void UpdateMovement(KeyboardState aCurrentKeyboardState)
        {

            if (mCurrentState == State.Falling)
            {
                mDirection.Y = MOVE_DOWN;
                mSpeed = new Vector2(WIZARD_SPEED, WIZARD_SPEED + 300);

                if (Position.Y > 462)
                {
                    Position.Y = 462;
                    mCurrentState = State.Walking;
                    mDirection = Vector2.Zero;
                }

                //if (mDirection.Y == MOVE_DOWN && Position.Y >= bridgepos.Y - 70 && Position.Y <= bridgepos.Y - 68 && Position.X >= bridgepos.X && Position.X <= bridgepos.X + 120)
                //{

                //    Position.Y = bridgepos.Y - 70;
                //    mCurrentState = State.Walking;
                //    mDirection = Vector2.Zero;

                //}
            }


            if (mCurrentState == State.Walking)
            {

                mSpeed = Vector2.Zero;
                mDirection = Vector2.Zero;



                if (aCurrentKeyboardState.IsKeyDown(Keys.A) == true)
                {

                    isFacingLeft = true;

                    mSpeed.X = WIZARD_SPEED;

                    mDirection.X = MOVE_LEFT;
                    Source = new Rectangle(k, 0, s, Source.Height);
                    m++;
                    if (m == 12)
                    {
                        m = 0;
                    }
                    if (k == 0 && s == 100&&m==4)
                    {
                        k = 100;
                        s = 130;
                    }
                    else if(k==100&&s==130&&m==8)
                    {
                        k=230;
                        s=100;
                    }


                    else if(m==11)
                    {
                        k = 0;
                        s = 100;
                    }
                    if (Position.X > 740)
                    {
                        Position.X = 740;
                    }
                    if (Position.X < 0)
                    {
                        Position.X = 0;
                    }

                }

                else if (aCurrentKeyboardState.IsKeyDown(Keys.D) == true)
                {

                    isFacingLeft = false;
                    mSpeed.X = WIZARD_SPEED;

                    mDirection.X = MOVE_RIGHT;



                    Source = new Rectangle(k, 0, s, Source.Height);
                    m++;
                    if(m==12)
                    {
                        m = 0;

                    }

                    if (k == 0 && s == 100&&m==4)
                    {
                        k = 100;
                        s = 130;
                    }
                    else if (k == 100 && s == 130&&m==8)
                    {
                        k = 230;
                        s = 100;
                    }


                    else if(m==11)
                    {
                        k = 0;
                        s = 100;
                    }

                }

                else
                {
                    Source = new Rectangle(0, 0, 100, Source.Height);
                }



            }



        }


        private void UpdateJump(KeyboardState aCurrentKeyboardState)
        {

            if (mCurrentState == State.Walking)
            {

                if ((aCurrentKeyboardState.IsKeyDown(Keys.O) || aCurrentKeyboardState.IsKeyDown(Keys.W)) && !(mPreviousKeyboardState.IsKeyDown(Keys.O) || mPreviousKeyboardState.IsKeyDown(Keys.W)))
                {

                    Jump();

                }

            }

            if (mCurrentState == State.Jumping)
            {
               if (mStartingPosition.Y - Position.Y > HOW_MUCH_JUMP)
                {
                    mDirection.Y = MOVE_DOWN;
                }



               else if (Position.Y > mStartingPosition.Y)
                {
                    Position.Y = mStartingPosition.Y;
                    mCurrentState = State.Walking;
                   mDirection = Vector2.Zero;
               }


            }

        }
        private void Jump()
        {

            if (mCurrentState != State.Jumping)
            {

                mCurrentState = State.Jumping;
                mStartingPosition = Position;
                mDirection.Y = MOVE_UP;
                mSpeed = new Vector2(WIZARD_SPEED, WIZARD_SPEED+300);

            }

        }

        private void UpdateFireball(GameTime theGameTime, KeyboardState aCurrentKeyboardState)
        {

            foreach (Fireball aFireball in mFireballs)
            {
                aFireball.Update(theGameTime);
            }


            if (aCurrentKeyboardState.IsKeyDown(Keys.I) == true && mPreviousKeyboardState.IsKeyDown(Keys.I) == false)
            {
                ShootFireball();

            }

        }

        private void ShootFireball()
        {

            if (mCurrentState == State.Walking)
            {
                bool aCreateNew = true;
                foreach (Fireball aFireball in mFireballs)
                {
                    if (aFireball.Visible == false)
                    {
                        aCreateNew = false;

                        aFireball.Fire(Position + new Vector2(size.Width / 2, size.Height / 2),
                        new Vector2(200, 0), isFacingLeft);
                        break;
                    }

                }

                if (aCreateNew == true)
                {
                    Fireball aFireball = new Fireball();
                    aFireball.LoadContent(mContentManager, @"images/Fireball");
                    aFireball.Fire(Position + new Vector2(size.Width / 2, size.Height / 2), new Vector2(200, 200),isFacingLeft);
                    mFireballs.Add(aFireball);
                }

            }

        }

        public override void Draw(SpriteBatch theSpriteBatch)
        {
            foreach (Fireball aFireball in mFireballs)
            {
                aFireball.Draw(theSpriteBatch);
            }
            base.Draw(theSpriteBatch);



            SpriteEffects spriteEffect = SpriteEffects.None;

            if (isFacingLeft)
            {
                spriteEffect = SpriteEffects.FlipHorizontally;
            }

            theSpriteBatch.Draw(mSpriteTexture, Position, Source, Color.White, 0.0f, Vector2.Zero, Scale, spriteEffect, 0);

        }

        public void if_player_on_bridge(Vector2 bridgepos)
        {

            if (mDirection.Y == MOVE_DOWN && Position.Y >=  Math.Abs(bridgepos.Y-75) && Position.Y <= Math.Abs(bridgepos.Y-65) && Position.X >= bridgepos.X-20 && Position.X <= bridgepos.X+155)
            {

                Position.Y = bridgepos.Y-70;
                mCurrentState = State.Walking;
                mDirection = Vector2.Zero;

            }
            if (Position.Y == bridgepos.Y-70 && (Position.X > bridgepos.X + 155 || Position.X < bridgepos.X-35) )
            {



                mCurrentState = State.Falling;
                mDirection.Y = MOVE_DOWN;
                //mCurrentState = State.Jumping;



            }


        }
        public bool Collides(Books book)
        {
            if (Position.X < book.BkPosition.X + 10 && Position.X > book.BkPosition.X - 10)
            {
                if (Position.Y < book.BkPosition.Y + 50 && Position.Y > book.BkPosition.Y - 50)
                //    Position.Y + size.Y > book.BkPosition.Y &&
                //    Position.Y < (book.BkPosition.Y + book.size.Y))
                {
                    return true;

                }
                else
                {
                    return false;
                }

            }

            else
            {
                return false;
            }
        }

        public bool Collides(Next_Level nl)
        {
            if (Position.X < nl.EPosition.X+10 && Position.X > nl.EPosition.X - 10)
            {
                if (Position.Y < nl.EPosition.Y + 60 && Position.Y > nl.EPosition.Y - 60)
                //    Position.Y + size.Y > book.BkPosition.Y &&
                //    Position.Y < (book.BkPosition.Y + book.size.Y))
                {
                return true;

                }
                else
                {
                    return false;
                }

            }

            else
            {
                return false;
            }
        }
        public bool Collides(Enemy enemy1)
        {
            if (Position.X < enemy1.EnemyPosition.X + 105 && Position.X > enemy1.EnemyPosition.X +5)
            {
                if (Position.Y < enemy1.EnemyPosition.Y + 150 && Position.Y > enemy1.EnemyPosition.Y - 50)
                {
                    return true;
                }

                else
                {
                    return false;
                }
            }

            else
            {
                return false;
            }
        }

        public bool Fireball_collides_with_enemy(Enemy e1)
        {
            foreach (Fireball aFireball in mFireballs)
            {
                if (aFireball.Visible == true)
                {
                    if (aFireball.FPosition.X > e1.EnemyPosition.X + 15 && aFireball.FPosition.X < e1.EnemyPosition.X + 35)
                    {
                        if (aFireball.FPosition.Y > e1.EnemyPosition.Y && aFireball.FPosition.Y < e1.EnemyPosition.Y + 120)
                        {

                            aFireball.Visible = false;
                            return true;

                        }
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
            return false;


        }

    }
}
