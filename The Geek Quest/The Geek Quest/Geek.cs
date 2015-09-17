using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.GamerServices;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Media;
using Microsoft.Xna.Framework.Net;
using Microsoft.Xna.Framework.Storage;


namespace The_Geek_Quest
{
    /// <summary>
    /// This is a game component that implements IUpdateable.
    /// </summary>
    public class Geek : Microsoft.Xna.Framework.DrawableGameComponent 
    {

        protected Texture2D texture;
        protected Rectangle spriteRectangle;
        protected Vector2 Position;
        // Width and height of sprite in texture
        protected const int GEEKWIDTH = 100;
        protected const int GEEKHEIGHT = 130;
        // Screen area
        protected Rectangle screenBounds;         // to get the bound of screen
        int k = 0, s = 100, m = 0;              // to animate while moving
        const int START_POSITION_X = 125;   // to move
        const int START_POSITION_Y = 445;
        const int WIZARD_SPEED = 160;
        const int MOVE_UP = -1;
        const int MOVE_DOWN = 1;
        const int MOVE_LEFT = -1;
        const int MOVE_RIGHT = 1;
        KeyboardState mPreviousKeyboardState;
        enum State
        {
            Walking,
            Jumping,
            Ducking

        }
        Vector2 mDirection = Vector2.Zero;
        Vector2 mSpeed = Vector2.Zero;
        State mCurrentState = State.Walking;
        bool isFacingLeft = false;

        Vector2 mStartingPosition = Vector2.Zero;

        public Geek(Game game, ref Texture2D theTexture)
            : base(game)
        {
            // TODO: Construct any child components here
            texture = theTexture;
            Position = new Vector2();
            spriteRectangle = new Rectangle(0, 0, GEEKWIDTH, GEEKHEIGHT);
        }

        /// <summary>
        /// Allows the game component to perform any initialization it needs to before starting
        /// to run.  This is where it can query for any required services and load content.
        /// </summary>
        public void PutinStartPosition()
        {
            Position.X = screenBounds.Width / 2;
            Position.Y = screenBounds.Height - GEEKHEIGHT;
        }
        public override void Initialize()
        {
            // TODO: Add your initialization code here

            base.Initialize();
        }

        /// <summary>
        /// Allows the game component to update itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        public override void Update(GameTime gameTime)
        {
            // TODO: Add your update code here
            KeyboardState aCurrentKeyboardState = Keyboard.GetState();
            UpdateMovement(aCurrentKeyboardState);
            UpdateJump(aCurrentKeyboardState);
            UpdateDuck(aCurrentKeyboardState);
            mPreviousKeyboardState = aCurrentKeyboardState;

            base.Update(gameTime);
        }
        private void UpdateMovement(KeyboardState aCurrentKeyboardState)
        {


            if (mCurrentState == State.Walking)
            {

                mSpeed = Vector2.Zero;

                mDirection = Vector2.Zero;



                if (aCurrentKeyboardState.IsKeyDown(Keys.Left) == true)
                {

                    isFacingLeft = true;

                    mSpeed.X = WIZARD_SPEED;

                    mDirection.X = MOVE_LEFT;
                    spriteRectangle  = new Rectangle(k, 0, s, GEEKHEIGHT);
                    m++;
                    if (m == 12)
                    {
                        m = 0;
                    }
                    if (k == 0 && s == 100 && m == 4)
                    {
                        k = 100;
                        s = 130;
                    }
                    else if (k == 100 && s == 130 && m == 8)
                    {
                        k = 230;
                        s = 100;
                    }


                    else if (m == 11)
                    {
                        k = 0;
                        s = 100;
                    }


                }

                else if (aCurrentKeyboardState.IsKeyDown(Keys.Right) == true)
                {

                    isFacingLeft = false;
                    mSpeed.X = WIZARD_SPEED;

                    mDirection.X = MOVE_RIGHT;



                    spriteRectangle = new Rectangle(k, 0, s, GEEKHEIGHT);
                    m++;
                    if (m == 12)
                    {
                        m = 0;

                    }

                    if (k == 0 && s == 100 && m == 4)
                    {
                        k = 100;
                        s = 130;
                    }
                    else if (k == 100 && s == 130 && m == 8)
                    {
                        k = 230;
                        s = 100;
                    }


                    else if (m == 11)
                    {
                        k = 0;
                        s = 100;
                    }

                }

                else
                {
                    spriteRectangle = new Rectangle(0, 0, 100, GEEKHEIGHT);
                }


            }

        }
        private void UpdateDuck(KeyboardState aCurrentKeyboardState)
        {
            if (aCurrentKeyboardState.IsKeyDown(Keys.RightShift))
            {
                Duck();
            }
            else
            {
                StopDucking();
            }

        }
        private void Duck()
        {
            if (mCurrentState == State.Walking)
            {

                mSpeed = Vector2.Zero;

                mDirection = Vector2.Zero;



                spriteRectangle = new Rectangle(100, 0, 130, GEEKHEIGHT);

                mCurrentState = State.Ducking;

            }
        }
        private void StopDucking()
        {

            if (mCurrentState == State.Ducking)
            {

                spriteRectangle = new Rectangle(0, 0, 100, GEEKHEIGHT);

                mCurrentState = State.Walking;

            }

        }



        private void UpdateJump(KeyboardState aCurrentKeyboardState)
        {

            if (mCurrentState == State.Walking)
            {

                if (aCurrentKeyboardState.IsKeyDown(Keys.Space) == true && mPreviousKeyboardState.IsKeyDown(Keys.Space) == false)
                {

                    Jump();

                }

            }



            if (mCurrentState == State.Jumping)
            {

                if (mStartingPosition.Y  - Position.Y > 150)
                {

                    mDirection.Y = MOVE_DOWN;

                }



                if (Position.Y > mStartingPosition.Y)
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
                mSpeed = new Vector2(WIZARD_SPEED, WIZARD_SPEED + 300);

            }
        }




        public override void Draw(GameTime gameTime)
        {
            // Get the current sprite batch
            SpriteBatch sBatch =
            (SpriteBatch)Game.Services.GetService(typeof(SpriteBatch));
            // Draw the ship

            sBatch.Draw(texture,Position, spriteRectangle, Color.White);
            base.Draw(gameTime);
        }
        /// <summary>
        /// Get the bound rectangle of ship position in screen
        /// </summary>
        public Rectangle GetBounds()
        {
            return new Rectangle((int)Position.X, (int)Position.Y,
            GEEKWIDTH, GEEKHEIGHT);
        }


    }
}