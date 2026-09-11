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
    class Enemy
    {
        //The current position of the Sprite



        public bool visible = true;



        public Vector2 EnemyPosition ;
        private float scale = 0.6f;
        public Rectangle size = new Rectangle();
        private Texture2D EnemySpriteTexture;
        private Vector2 startpos=new Vector2();
        private Vector2 speed=new Vector2 (100,0);
        private Vector2  direction =new Vector2 (-1,0);
        private SpriteEffects seffects=SpriteEffects.FlipHorizontally;
        public int How_much_to_Left = 200;
        public int How_much_to_Right = 50;
        public int no_of_hits = 0;
        public String  Enemy_image=@"images/Enemy";
        public float Scale
        {
            get
            {
                return scale;
            }
            set
            {
                scale = value;
                size = new Rectangle(0, 0, (int)((EnemySpriteTexture.Width) * scale), (int)((EnemySpriteTexture.Height) * scale));
            }
        }

        public Enemy(Vector2 EnemyStartPosition)
        {
            EnemyPosition = EnemyStartPosition;
            startpos = EnemyStartPosition;
        }



        //Load the texture for the sprite using the Content Pipeline

        public void LoadContent(ContentManager theContentManager)
        {

            EnemySpriteTexture = theContentManager.Load<Texture2D>(Enemy_image);

        }

        public void Update(GameTime theGameTime)
        {
            EnemyPosition+= speed*direction*(float)theGameTime.ElapsedGameTime.TotalSeconds;;
            if (EnemyPosition.X < (startpos.X - How_much_to_Left))
            {

                direction = new Vector2(1, 0);
                seffects = SpriteEffects.None;

            }
            if (EnemyPosition.X > (startpos.X + How_much_to_Right))
            {
                direction = new Vector2(-1, 0);
                seffects = SpriteEffects.FlipHorizontally;
            }

        }


        public void Draw(SpriteBatch theSpriteBatch)
        {


            theSpriteBatch.Draw(EnemySpriteTexture, EnemyPosition,
                                new Rectangle(0, 0, EnemySpriteTexture.Width, EnemySpriteTexture.Height),
                                  Color.White, 0.0f, Vector2.Zero, scale, seffects, 0);


        }




    }
}
