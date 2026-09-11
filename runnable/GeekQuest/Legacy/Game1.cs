using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Media;

namespace CreatingA2DSprite
{
    /// <summary>
    /// This is the main type for your game
    /// </summary>
    public class Game1 : Microsoft.Xna.Framework.Game
    {
        // variables for screen

        enum Screen
        {
            Main_Menu,
            Game,
            Pause,
            Game_Over,
            Game_Comp,
            Credits,
            Help
        }
        Screen currentscreen = Screen.Main_Menu;

        //Variables For Mouse in front Screen
        enum BState
        {
            HOVER,
            UP,
            JUST_RELEASED,
            DOWN
        }
        const int NUMBER_OF_BUTTONS = 2,
            START_BUTTON_INDEX = 0,
            EXIT_BUTTON_INDEX = 1,
            BUTTON_HEIGHT = 100,
            BUTTON_WIDTH = 200;


        Color background_color;
        Color[] button_color = new Color[NUMBER_OF_BUTTONS];
        Rectangle[] button_rectangle = new Rectangle[NUMBER_OF_BUTTONS];
        BState[] button_state = new BState[NUMBER_OF_BUTTONS];
        Texture2D[] button_texture = new Texture2D[NUMBER_OF_BUTTONS];
        double[] button_timer = new double[NUMBER_OF_BUTTONS];
        //mouse pressed and mouse just pressed
        bool mpressed, prev_mpressed = false;
        //mouse location in window
        int mx, my;
        double frame_time;
        SpriteFont gameFont,endFont;

        //Variables For Mouse End

        //Variables For Sound

        SoundEffectInstance sbacksound;         // Start Screen Sound
        SoundEffectInstance gbacksound;          // Game Play Sound

        //Variables For Sound End
        //test variables
        //bool at_help_screen = false;// help screen shown
        //testvariables end
        //Game Basic Variables
        public GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;
        //Game Basic Variables End

        // Objects for textures,sprites etc.
        Wizard player_geek;
        Background  back1 = new Background();
        Texture2D name, chatur_forStart,initbacksprite,pausepic,gameoverpic,gamecomppic,creditpic,helppic;
        Vector2 bridgePosition = new Vector2(250, 100);
        Bridge[] bridges;
        Books[] book;
        Next_Level nl;
        int NO_OF_BRIDGES = 0;   // total number of bridges in a level
        int NO_OF_BOOKS = 0;       // total number of books in a level
        int how_many_taken = 0;         // tells how many books have been taken
        int NO_OF_ENEMY = 0;
         bool change_level = false;
          int score = 0;
        int level = 1;
        int lives=3;
        // Objects for textures,sprites etc. End

        Enemy[] enemies;




        public Game1()//constructor
        {
            graphics = new GraphicsDeviceManager(this);
            Content = new ArchiveContentManager(Services);
            graphics.PreferredBackBufferWidth = 800;
            graphics.PreferredBackBufferHeight = 600;

        }

        /// <summary>
        /// Allows the game to perform any initialization it needs to before starting to run.
        /// This is where it can query for any required services and load any non-graphic
        /// related content.  Calling base.Initialize will enumerate through any components
        /// and initialize them as well.
        /// </summary>
        protected override void Initialize()//to initialise buttons,screen size,Audio
        {
            // starting x and y locations to stack buttons
            // vertically in the middle of the screen
            int x = Window.ClientBounds.Width / 2 - BUTTON_WIDTH / 2;
            int y = Window.ClientBounds.Height / 2 -
                NUMBER_OF_BUTTONS / 2 * BUTTON_HEIGHT -
                (NUMBER_OF_BUTTONS % 2) * BUTTON_HEIGHT / 2;
            for (int i = 0; i < NUMBER_OF_BUTTONS; i++)
            {
                button_state[i] = BState.UP;
                button_color[i] = Color.White;
                button_timer[i] = 0.0;
                button_rectangle[i] = new Rectangle(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
                y += BUTTON_HEIGHT;
            }
            IsMouseVisible = true;
            background_color = Color.White;

            // Assume the default names for the wave and sound banks.
            // To change these names, change properties in XACT.

            Window.Title = "The Geek Quest";
            graphics.PreferredBackBufferWidth = 800;
            graphics.PreferredBackBufferHeight = 600;

            sbacksound = Content.Load<SoundEffect>("Sounds/Start").CreateInstance();
            gbacksound = Content.Load<SoundEffect>("Sounds/Gameplay_music").CreateInstance();

            sbacksound.IsLooped = true;
            gbacksound.IsLooped = true;
            sbacksound.Play();
            gbacksound.Play();
            gbacksound.Pause();

            base.Initialize();
        }

        /// <summary>
        /// LoadContent will be called once per game and is the place to load
        /// all of your content.
        /// </summary>
        protected override void LoadContent()
        {
            //Loading content for Main Menu

            button_texture[START_BUTTON_INDEX] =  Content.Load<Texture2D>(@"images/start");
            button_texture[EXIT_BUTTON_INDEX] =   Content.Load<Texture2D>(@"images/exit");
            name = this.Content.Load<Texture2D>(@"images/name");
            chatur_forStart = this.Content.Load<Texture2D>(@"images/Geek");
            //For Font
            gameFont = this.Content.Load<SpriteFont>("font");
            endFont = this.Content.Load<SpriteFont>("font_1");
            // Create a new SpriteBatch, which can be used to draw textures.
            spriteBatch = new SpriteBatch(GraphicsDevice);

            // TODO: use this.Content to load your game content here
            pausepic = this.Content.Load<Texture2D>(@"images/Paused");
            gameoverpic = this.Content.Load<Texture2D>(@"images/gameover");
            gamecomppic = this.Content.Load<Texture2D>(@"images/gamecomp");
             creditpic = this.Content.Load<Texture2D>(@"images/credits");
           helppic = this.Content.Load<Texture2D>(@"images/help");

            initbacksprite = this.Content.Load<Texture2D>(@"images/initscreen");
      //      player_geek.LoadContent(this.Content); //loading the character





        }
        public void level1()
        {
            player_geek = new Wizard();
            player_geek.LoadContent(this.Content); //loading the character
//defining enemy constraints
            enemies = new Enemy[2];
            NO_OF_ENEMY = 2;
            enemies[0] = new Enemy(new Vector2(634,403));
            enemies[0].LoadContent(this.Content);
            enemies[0].How_much_to_Left = 200;
            enemies[0].How_much_to_Right = 50;
            enemies[1] = new Enemy(new Vector2(303,-45));
            enemies[1].LoadContent(this.Content);
            enemies[1].How_much_to_Left = 100;
            enemies[1].How_much_to_Right = 45;
//enemy constraints end
           bridges = new Bridge[5];
              NO_OF_BRIDGES = 5;
            bridges[0] = new Bridge();
            bridges[1] = new Bridge();
            bridges[2] = new Bridge();
            bridges[3] = new Bridge();
            bridges[4] = new Bridge();
            book = new Books[7];
            NO_OF_BOOKS = 7;
            book[0] = new Books();
            book[1] = new Books();
            book[2] = new Books();
            book[3] = new Books();
            book[4] = new Books();
            book[5] = new Books();
            book[6] = new Books();
            nl = new Next_Level();
            //Background

            back1.LoadContent(this.Content, @"images/Background01");
            back1.Scale = 0.9f;

            //Background_end
           //Briges begin
            bridges[0].LoadContent(this.Content, @"images/bridge");
            bridges[0].Scale = 0.65f;
            bridges[0].BPosition = new Vector2(150, 400);
            bridges[1].LoadContent(this.Content, @"images/bridge_1");
            bridges[1].Scale = 0.65f;
            bridges[1].BPosition = new Vector2(300, 300);
            bridges[2].LoadContent(this.Content, @"images/bridge_1");
            bridges[2].Scale = 0.65f;
            bridges[2].BPosition = new Vector2(100, 170);

            bridges[3].LoadContent(this.Content, @"images/bridge");
            bridges[3].Scale = 0.65f;
            bridges[3].BPosition = new Vector2(250, 100);
            bridges[4].LoadContent(this.Content, @"images/bridge_1");
            bridges[4].Scale = 0.65f;
            bridges[4].BPosition = new Vector2(525, 110);
            //bridges end

            book[0].LoadContent(this.Content, @"images/books");
            book[0].Scale = 0.099f;
            book[0].BkPosition = new Vector2(250, 375);
            book[1].LoadContent(this.Content, @"images/books");
            book[1].Scale = 0.099f;
            book[1].BkPosition = new Vector2(350, 275);
            book[2].LoadContent(this.Content, @"images/books");
            book[2].Scale = 0.099f;
            book[2].BkPosition = new Vector2(600, 325);
            book[3].LoadContent(this.Content, @"images/books");
            book[3].Scale = 0.099f;
            book[3].BkPosition = new Vector2(150, 145);
            book[4].LoadContent(this.Content, @"images/books");
            book[4].Scale = 0.099f;
            book[4].BkPosition = new Vector2(350, 75);
            book[5].LoadContent(this.Content, @"images/books");
            book[5].Scale = 0.099f;
            book[5].BkPosition = new Vector2(550, 85);
            book[6].LoadContent(this.Content, @"images/books");
            book[6].Scale = 0.099f;
            book[6].BkPosition = new Vector2(100, 275);
            nl.LoadContent(this.Content, @"images/nextlevel");
            nl.EPosition = new Vector2(675, 465);
            nl.Scale = 1.0f;

        }
        public void level2()
        {
            player_geek = new Wizard();
            player_geek.LoadContent(this.Content); //loading the character

            enemies = new Enemy[3];
            NO_OF_ENEMY = 3;
            enemies[0] = new Enemy(new Vector2(334, 285));
            enemies[0].LoadContent(this.Content);
            enemies[0].How_much_to_Left = 60;
            enemies[0].How_much_to_Right = 60;
            enemies[1] = new Enemy(new Vector2(128, 7));
            enemies[1].LoadContent(this.Content);
            enemies[1].How_much_to_Left = 120;
            enemies[1].How_much_to_Right = 25;
            enemies[2] = new Enemy(new Vector2(628, 109));
            enemies[2].LoadContent(this.Content);
            enemies[2].How_much_to_Left = 120;
            enemies[2].How_much_to_Right = 25;
            how_many_taken = 0;
            bridges = new Bridge[7];
            NO_OF_BRIDGES = 7;
            bridges[0] = new Bridge();
            bridges[1] = new Bridge();
            bridges[2] = new Bridge();
            bridges[3] = new Bridge();
            bridges[4] = new Bridge();
            bridges[5] = new Bridge();
            bridges[6] = new Bridge();

            book = new Books[10];
            NO_OF_BOOKS = 10;
            book[0] = new Books();
            book[1] = new Books();
            book[2] = new Books();
            book[3] = new Books();
            book[4] = new Books();
            book[5] = new Books();
            book[6] = new Books();
            book[7] = new Books();
            book[8] = new Books();
            book[9] = new Books();

            nl = new Next_Level();
            //Background
            back1.LoadContent(this.Content, @"images/Background02");
            back1.Scale = 0.9f;
            //Background_end

            bridges[0].LoadContent(this.Content, @"images/bridge");
            bridges[0].Scale = 0.65f;
            bridges[0].BPosition = new Vector2(50, 400);
            bridges[1].LoadContent(this.Content, @"images/bridge_1");
            bridges[1].Scale = 0.65f;
            bridges[1].BPosition = new Vector2(50, 250);
            bridges[2].LoadContent(this.Content, @"images/bridge_2");
            bridges[2].Scale = 0.65f;
            bridges[2].BPosition = new Vector2(50, 150);

            bridges[3].LoadContent(this.Content, @"images/bridge_2");
            bridges[3].Scale = 0.65f;
            bridges[3].BPosition = new Vector2(550, 401);
            bridges[4].LoadContent(this.Content, @"images/bridge_1");
            bridges[4].Scale = 0.65f;
            bridges[4].BPosition = new Vector2(550, 251);
            bridges[5].LoadContent(this.Content, @"images/bridge_2");
            bridges[5].Scale = 0.65f;
            bridges[5].BPosition = new Vector2(550, 151);



            bridges[6].LoadContent(this.Content, @"images/bridge_2");
            bridges[6].Scale = 0.65f;
            bridges[6].BPosition = new Vector2(300, 430);


            for (int i = 0; i < 10; i++)
            {
                book[i].LoadContent(this.Content, @"images/books");
                book[i].Scale = 0.099f;
            }
            book[0].BkPosition = new Vector2(285, 250);

            book[1].BkPosition = new Vector2(375, 249);

            book[2].BkPosition = new Vector2(465, 251);

            book[3].BkPosition = new Vector2(286, 151);

            book[4].BkPosition = new Vector2(376, 150);

            book[5].BkPosition = new Vector2(466, 148);

            book[6].BkPosition = new Vector2(25, 45);

            book[7].BkPosition = new Vector2(670, 46);
            book[8].BkPosition = new Vector2(300, 25);
            book[9].BkPosition = new Vector2(480, 26);
            nl.LoadContent(this.Content, @"images/nextlevel");
            nl.EPosition = new Vector2(350, 382);
            nl.Scale = 1.0f;
        }
     public void level3()
     {
         player_geek = new Wizard();
         player_geek.LoadContent(this.Content); //loading the character
         enemies = new Enemy[5];
         NO_OF_ENEMY = 5;
         enemies[0] = new Enemy(new Vector2(434, 385));
         enemies[0].LoadContent(this.Content);
         enemies[0].How_much_to_Left =80;
         enemies[0].How_much_to_Right = 80;
         enemies[1] = new Enemy(new Vector2(581, 386));
         enemies[1].LoadContent(this.Content);
         enemies[1].How_much_to_Left = 100;
         enemies[1].How_much_to_Right = 90;
         enemies[2] = new Enemy(new Vector2(583, -44));
         enemies[2].LoadContent(this.Content);
         enemies[2].How_much_to_Left = 100;
         enemies[2].How_much_to_Right = 50;
         enemies[3] = new Enemy(new Vector2(151, 76));
         enemies[3].LoadContent(this.Content);
         enemies[3].How_much_to_Left = 90;
         enemies[3].How_much_to_Right = 50;
         enemies[4] = new Enemy(new Vector2(23, -64));
         enemies[4].LoadContent(this.Content);
         enemies[4].How_much_to_Left = 70;
         enemies[4].How_much_to_Right = 90;
            how_many_taken = 0;
            bridges = new Bridge[6];
            NO_OF_BRIDGES = 6;
            bridges[0] = new Bridge();
            bridges[1] = new Bridge();
            bridges[2] = new Bridge();
            bridges[3] = new Bridge();
            bridges[4] = new Bridge();
            bridges[5] = new Bridge();
            book = new Books[11];
            NO_OF_BOOKS = 11;
            book[0] = new Books();
            book[1] = new Books();
            book[2] = new Books();
            book[3] = new Books();
            book[4] = new Books();
            book[5] = new Books();
            book[6] = new Books();
            book[7] = new Books();
            book[8] = new Books();
            book[9] = new Books();
            book[10] = new Books();


            nl = new Next_Level();
            //Background
            back1.LoadContent(this.Content, @"images/Background03");
            back1.Scale = 0.9f;
            //Background_end

            bridges[0].LoadContent(this.Content, @"images/bridge_2");
            bridges[0].Scale = 0.65f;
            bridges[0].BPosition = new Vector2(150, 400);
            bridges[1].LoadContent(this.Content, @"images/bridge_1");
            bridges[1].Scale = 0.65f;
            bridges[1].BPosition = new Vector2(275, 300);
            bridges[2].LoadContent(this.Content, @"images/bridge_1");
            bridges[2].Scale = 0.65f;
            bridges[2].BPosition = new Vector2(375, 200);
            bridges[3].LoadContent(this.Content, @"images/bridge_2");
            bridges[3].Scale = 0.65f;
            bridges[3].BPosition = new Vector2(525, 100);
            bridges[4].LoadContent(this.Content, @"images/bridge");
            bridges[4].Scale = 0.65f;
            bridges[4].BPosition = new Vector2(100, 220);
            bridges[5].LoadContent(this.Content, @"images/bridge_1");
            bridges[5].Scale = 0.65f;
            bridges[5].BPosition = new Vector2(10, 80);


            book[0].LoadContent(this.Content, @"images/books");
            book[0].Scale = 0.099f;
            book[0].BkPosition = new Vector2(250, 375);
            book[1].LoadContent(this.Content, @"images/books");
            book[1].Scale = 0.099f;
            book[1].BkPosition = new Vector2(350, 275);
            book[2].LoadContent(this.Content, @"images/books");
            book[2].Scale = 0.099f;
            book[2].BkPosition = new Vector2(600, 325);
            book[3].LoadContent(this.Content, @"images/books");
            book[3].Scale = 0.099f;
            book[3].BkPosition = new Vector2(700, 445);
            book[4].LoadContent(this.Content, @"images/books");
            book[4].Scale = 0.099f;
            book[4].BkPosition = new Vector2(350, 75);
            book[5].LoadContent(this.Content, @"images/books");
            book[5].Scale = 0.099f;
            book[5].BkPosition = new Vector2(550, 72);
            book[6].LoadContent(this.Content, @"images/books");
            book[6].Scale = 0.099f;
            book[6].BkPosition = new Vector2(100, 275);
            book[7].LoadContent(this.Content, @"images/books");
            book[7].Scale = 0.099f;
            book[7].BkPosition = new Vector2(600, 440);
            book[8].LoadContent(this.Content, @"images/books");
            book[8].Scale = 0.099f;
            book[8].BkPosition = new Vector2(690, 323);
            book[9].LoadContent(this.Content, @"images/books");
            book[9].Scale = 0.099f;
            book[9].BkPosition = new Vector2(115, 56);
            book[10].LoadContent(this.Content, @"images/books");
            book[10].Scale = 0.099f;
            book[10].BkPosition = new Vector2(455, 175);

            nl.LoadContent(this.Content, @"images/nextlevel");
            nl.EPosition = new Vector2(30,32);
            nl.Scale = 1.0f;
     }

        /// <summary>
        /// UnloadContent will be called once per game and is the place to unload
        /// all content.
        /// </summary>
        protected override void UnloadContent()
        {
            // TODO: Unload any non ContentManager content


        }

        /// <summary>
        /// Allows the game to run logic such as updating the world,
        /// checking for collisions, gathering input, and playing audio.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Update(GameTime gameTime)
        {
            if (Keyboard.GetState().IsKeyDown(Keys.Escape)) { Exit(); return; }

            switch(currentscreen)
            {
                case Screen.Main_Menu:
                    gbacksound.Pause();
                    sbacksound.Resume();
            // get elapsed frame time in seconds
            frame_time = gameTime.ElapsedGameTime.Milliseconds / 1000.0;

            // update mouse variables
            MouseState mouse_state = Mouse.GetState();
            mx = mouse_state.X;
            my = mouse_state.Y;
            prev_mpressed = mpressed;
            mpressed = mouse_state.LeftButton == ButtonState.Pressed;
            update_buttons();
            if (Keyboard.GetState().IsKeyDown(Keys.H))
                currentscreen = Screen.Help;
            break;

         case Screen.Game:
                 // TODO: Add your update logic here

                sbacksound.Pause();
                gbacksound.Resume();
                Update_Game(gameTime); // To Update the game
                if (Keyboard.GetState().IsKeyDown(Keys.P))
                    currentscreen = Screen.Pause;
                 break;

         case Screen.Pause:
                 gbacksound.Pause();
                 sbacksound.Resume();
                 if (Keyboard.GetState().IsKeyDown(Keys.R))
                     currentscreen = Screen.Game;
                 else
                     if (Keyboard.GetState().IsKeyDown(Keys.E))
                         currentscreen = Screen.Main_Menu;
                 break;

        case Screen.Game_Over:
                 gbacksound.Pause();
                 sbacksound.Resume();
                 if (Keyboard.GetState().IsKeyDown(Keys.E))
                 currentscreen = Screen.Main_Menu;
                 break;
        case Screen.Game_Comp:
                 gbacksound.Pause();
                 sbacksound.Resume();
                 if (Keyboard.GetState().IsKeyDown(Keys.E))
                     currentscreen = Screen.Main_Menu;
                 else if (Keyboard.GetState().IsKeyDown(Keys.C))
                     currentscreen = Screen.Credits;
                 break;
        case Screen.Credits:
                 if (Keyboard.GetState().IsKeyDown(Keys.E))
                     currentscreen = Screen.Main_Menu;
                 break;

         case Screen.Help:
                    if (Keyboard.GetState().IsKeyDown(Keys.E))
                     currentscreen = Screen.Main_Menu;
                 break;


            }

            base.Update(gameTime);
        }

        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        ///

        private void Update_Game(GameTime gameTime)
        {
            player_geek.Update(gameTime);//updating player movements ,jumps ets
            for (int en = 0; en < NO_OF_ENEMY; en++)
            {
                enemies[en].Update(gameTime);
            }
            //to check if on the bridge

            for (int I = 0; I < NO_OF_BRIDGES; I++)
            {
                player_geek.if_player_on_bridge(bridges[I].BPosition);
            }

            //to check if book taken
            for (int i = 0; i < NO_OF_BOOKS; i++)
            {
                if (player_geek.Collides(book[i]))
                {
                    if (book[i].visible == true)
                    {
                        score = score + 100;
                        how_many_taken++;
                        book[i].visible = false;
                    }


                }
            }

            //checking if enemy touches
            for (int en = 0; en < NO_OF_ENEMY; en++)
            {

                if (enemies[en].visible)
                {
                    enemies[en].LoadContent(this.Content);
                    //Loaded here to update the image

                    if (player_geek.Fireball_collides_with_enemy(enemies[en]))
                    {
                        enemies[en].no_of_hits++;
                        score += 20;
                        switch (enemies[en].no_of_hits)
                        {
                            case 1: enemies[en].Enemy_image = @"images/Enemy_Hit1";

                                break;
                            case 2: enemies[en].Enemy_image = @"images/Enemy_Hit2";

                                break;
                            case 3: enemies[en].Enemy_image = @"images/Enemy_Hit3";

                                break;
                            case 4: enemies[en].Enemy_image = @"images/Enemy_Hit4";
                                break;

                            case 5: enemies[en].Enemy_image = @"images/Enemy_Hit5";

                                break;
                            case 6: enemies[en].visible = false;
                                score = score + 80;
                                break;
                            //default: enemies[en].visible = false;
                            //    break;
                        }
                    }


                }

            }

            for (int en = 0; en < NO_OF_ENEMY; en++)
            {
                if (enemies[en].visible)
                {

                    if (player_geek.Collides(enemies[en]))
                    {
                        lives--;
                        how_many_taken = 0;
                        if (lives == 0)
                        {
                            currentscreen=Screen.Game_Over;
                        }
                        else
                        {
                            switch (level)
                            {
                                case 1: level1();
                                    break;
                                case 2: level2();
                                    break;
                                case 3: level3();
                                    break;

                            }
                        }


                    }
                }

            }

            if(player_geek.Collides(nl))
            {
            if (how_many_taken == NO_OF_BOOKS)

            {
                level++;
                change_level = true;

            }
            }
            if (change_level)
            {
                //UnloadContent();
                change_level = false;
                switch (level)
                {

                    case 2: level2();
                        break;
                    case 3: level3();
                        break;
                    case 4: currentscreen = Screen.Game_Comp;
                        break;
                }
            }



        }




        protected override void Draw(GameTime gameTime)
        {

            switch (currentscreen)
            {
                case Screen.Main_Menu:   // if at main menu drawing main menu

                    GraphicsDevice.Clear(Color.White);
                    spriteBatch.Begin();
                    spriteBatch.Draw(initbacksprite, new Rectangle(0, 0, (int)((initbacksprite.Width) * 2), (int)((initbacksprite.Height) * 2)), Color.White);//for early background...change this.....

                    for (int i = 0; i < NUMBER_OF_BUTTONS; i++)
                        spriteBatch.Draw(button_texture[i], button_rectangle[i], button_color[i]);
                    spriteBatch.Draw(name, new Vector2(Window.ClientBounds.Width / 2 - name.Width / 2, 0), Color.White);
                    spriteBatch.Draw(chatur_forStart, new Vector2(Window.ClientBounds.Width / 2 - button_rectangle[0].Width, graphics.PreferredBackBufferHeight - 220), new Rectangle(0, 0, 100, chatur_forStart.Height), Color.White, 0.0f, Vector2.Zero, 1.0f, SpriteEffects.None, 0);
                    spriteBatch.Draw(chatur_forStart, new Vector2(Window.ClientBounds.Width / 2 + button_rectangle[0].Width - 100, graphics.PreferredBackBufferHeight - 220), new Rectangle(0, 0, 100, chatur_forStart.Height), Color.White, 0.0f, Vector2.Zero, 1.0f, SpriteEffects.FlipHorizontally, 0);
                    spriteBatch.End();
                    break;

                case Screen.Game:           //If at game drawing game screen

                    graphics.GraphicsDevice.Clear(Color.CornflowerBlue);
                    spriteBatch.Begin();
                    back1.Draw(spriteBatch);


                    for (int k = 0; k < NO_OF_BRIDGES; k++)
                    {
                        bridges[k].Draw(this.spriteBatch);
                    }
                    for (int j = 0; j < NO_OF_BOOKS; j++)
                    {
                        if (book[j].visible == true)
                        {
                            book[j].Draw(this.spriteBatch);
                        }
                    }
                    nl.Draw(this.spriteBatch);
                    player_geek.Draw(this.spriteBatch);

                    for (int en = 0; en < NO_OF_ENEMY; en++)
                    {
                        if (enemies[en].visible == true)
                        {
                            enemies[en].Draw(this.spriteBatch);
                        }
                    }

                    if (level < 3)
                    {
                        spriteBatch.DrawString(gameFont, "SESSIONAL : " + level, new Vector2(340, 10), Color.White);
                    }
                    else
                    {
                        spriteBatch.DrawString(gameFont, "SEMESTER  ", new Vector2(340, 10), Color.White);
                    }

                    spriteBatch.DrawString(gameFont, "CREDITS : " + score, new Vector2(640, 10), Color.White);
                    spriteBatch.DrawString(gameFont, "LIVES : " + lives, new Vector2(100, 10), Color.White);
                    spriteBatch.End();
                    break;


                case Screen.Pause:  // If at Paused drawing paused screen
                    spriteBatch.Begin();

                    spriteBatch.Draw(pausepic, new Rectangle(0, 0, (int)((pausepic.Width) * 2), (int)((pausepic.Height) * 2)), Color.White);
                    spriteBatch.End();
                    break;

                case Screen.Game_Over:     // If Game Over Show Game over Screen
                    spriteBatch.Begin();

                    spriteBatch.Draw(gameoverpic, new Rectangle(0, 0, (int)((pausepic.Width) * 2), (int)((pausepic.Height) * 2)), Color.White);
                    spriteBatch.DrawString(endFont,"" +score, new Vector2(415, 240), Color.Black);

                    spriteBatch.End();
                    break;

                case Screen.Game_Comp:
                    spriteBatch.Begin();
                    spriteBatch.Draw(gamecomppic, new Rectangle(0, 0, (int)((gamecomppic.Width) * 2), (int)((gamecomppic.Height) * 2)), Color.White);
                    spriteBatch.DrawString(endFont, "" + score, new Vector2(315, 245), Color.Black);
                    spriteBatch.End();
                    break;


                case Screen.Credits:
                    spriteBatch.Begin();
                    spriteBatch.Draw(creditpic, new Rectangle(0, 0, (int)((creditpic.Width) * 2), (int)((creditpic.Height) * 2)), Color.White);
                     spriteBatch.End();
                    break;

                case Screen.Help:
                    spriteBatch.Begin();
                    spriteBatch.Draw(helppic, new Rectangle(0, 0, (int)((helppic.Width) * 2), (int)((helppic.Height) * 2)), Color.White);
                    spriteBatch.End();
                    break;
            }


            base.Draw(gameTime);


        }


        Boolean hit_image_alpha(Rectangle rect, Texture2D tex, int x, int y)
        {
            return hit_image_alpha(0, 0, tex, tex.Width * (x - rect.X) /
                rect.Width, tex.Height * (y - rect.Y) / rect.Height);
        }

        // wraps hit_image then determines if hit a transparent part of image
        Boolean hit_image_alpha(float tx, float ty, Texture2D tex, int x, int y)
        {
            if (hit_image(tx, ty, tex, x, y))
            {
                uint[] data = new uint[tex.Width * tex.Height];
                tex.GetData<uint>(data);
                if ((x - (int)tx) + (y - (int)ty) *
                    tex.Width < tex.Width * tex.Height)
                {
                    return ((data[
                        (x - (int)tx) + (y - (int)ty) * tex.Width
                        ] &
                                0xFF000000) >> 24) > 20;
                }
            }
            return false;
        }

        // determine if x,y is within rectangle formed by texture located at tx,ty
        Boolean hit_image(float tx, float ty, Texture2D tex, int x, int y)
        {
            return (x >= tx &&
                x <= tx + tex.Width &&
                y >= ty &&
                y <= ty + tex.Height);
        }

        // determine state and color of button
        void update_buttons()
        {
            for (int i = 0; i < NUMBER_OF_BUTTONS; i++)
            {

                if (hit_image_alpha(
                    button_rectangle[i], button_texture[i], mx, my))
                {
                    button_timer[i] = 0.0;
                    if (mpressed)
                    {
                        // mouse is currently down
                        button_state[i] = BState.DOWN;
                        button_color[i] = Color.Blue;
                    }
                    else if (!mpressed && prev_mpressed)
                    {
                        // mouse was just released
                        if (button_state[i] == BState.DOWN)
                        {
                            // button i was just down
                            button_state[i] = BState.JUST_RELEASED;
                        }
                    }
                    else
                    {
                        button_state[i] = BState.HOVER;
                        button_color[i] = Color.LightBlue;
                    }
                }
                else
                {
                    button_state[i] = BState.UP;
                    if (button_timer[i] > 0)
                    {
                        button_timer[i] = button_timer[i] - frame_time;
                    }
                    else
                    {
                        button_color[i] = Color.White;
                    }
                }

                if (button_state[i] == BState.JUST_RELEASED)
                {
                    take_action_on_button(i);
                }
            }
        }

        // Logic for each button click goes here
        void take_action_on_button(int i)
        {
            //take action corresponding to which button was clicked
            switch (i)
            {
                case START_BUTTON_INDEX:
                    score = 0;
                    lives = 3;
                    level = 1;
                    how_many_taken = 0;
                    level1();
                    currentscreen = Screen.Game;

                    break;
                case EXIT_BUTTON_INDEX:
                    this.Exit();
                    break;
                default:
                    break;
            }
        }
//Main Menu - Functions for Mouse - End









        public static bool IntersectPixels(Rectangle rectangleA, Color[] dataA, Rectangle rectangleB, Color[] dataB)
        {
            // Find the bounds of the rectangle intersection
            int top = Math.Max(rectangleA.Top, rectangleB.Top);
            int bottom = Math.Min(rectangleA.Bottom, rectangleB.Bottom);
            int left = Math.Max(rectangleA.Left, rectangleB.Left);
            int right = Math.Min(rectangleA.Right, rectangleB.Right);

            // Check every point within the intersection bounds
            for (int y = top; y < bottom; y++)
            {
                for (int x = left; x < right; x++)
                {
                    // Get the color of both pixels at this point
                    Color colorA = dataA[(x - rectangleA.Left) +
                                         (y - rectangleA.Top) * rectangleA.Width];
                    Color colorB = dataB[(x - rectangleB.Left) +
                                         (y - rectangleB.Top) * rectangleB.Width];

                    // If both pixels are not completely transparent,
                    if (colorA.A != 0 & colorB.A != 0)
                    {
                        // then an intersection has been found
                        return true;
                    }
                }
            }

            // No intersection found
            return false;
        }
    }
};
