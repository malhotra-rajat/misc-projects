using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Storage;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;


namespace CreatingA2DSprite
{
    class Next_Level
    {


        //The current position of the Sprite
        private float scale = 1.0f;
        public Rectangle size = new Rectangle();
        public Vector2 EPosition;
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



        //The texture object used when drawing the sprite

        private Texture2D mSpriteTexture;



        //Load the texture for the sprite using the Content Pipeline

        public void LoadContent(ContentManager theContentManager, string theAssetName)
        {

            mSpriteTexture = theContentManager.Load<Texture2D>(theAssetName);


        }



        public void Draw(SpriteBatch theSpriteBatch)
        {


            theSpriteBatch.Draw(mSpriteTexture, EPosition, new Rectangle(0, 0, mSpriteTexture.Width, mSpriteTexture.Height), Color.White, 0.0f, Vector2.Zero, scale, SpriteEffects.None, 0);


        }



    }

}













