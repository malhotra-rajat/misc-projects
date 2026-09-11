using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;

namespace CreatingA2DSprite
{
    class Books
    {
        public bool visible = true;

        private Texture2D mSpriteTexture;
        public Vector2 BkPosition;
        public Rectangle size = new Rectangle();
        private float scale = 1.0f;
        public float Scale
        {
            get
            {
                return scale;
            }
            set
            {
                scale = value;
                size = new Rectangle(0, 0, (int)((mSpriteTexture.Width) * Scale), (int)((mSpriteTexture.Height) * Scale));
            }
        }


        public void LoadContent(ContentManager theContentManager, string theAssetName)
        {
            mSpriteTexture = theContentManager.Load<Texture2D>(theAssetName);
        }

        public void Draw(SpriteBatch theSpriteBatch)
        {


            theSpriteBatch.Draw(mSpriteTexture, BkPosition, new Rectangle(0, 0, mSpriteTexture.Width, mSpriteTexture.Height), Color.White, 0.0f, Vector2.Zero, scale, SpriteEffects.None, 0);

        }


    }
}
