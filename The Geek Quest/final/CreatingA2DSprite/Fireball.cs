using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;
using System;

namespace CreatingA2DSprite
{
    class Fireball
    {
        private Texture2D mSpriteTexture;

        public Vector2 FPosition = new Vector2(0, 0);
        float MAX_DISTANCE = 300;
        public bool Visible = false;
        Vector2 FStartPosition;
        Vector2 FSpeed;
        Vector2 FDirection;
        Vector2 WPosition ;
        SpriteEffects Feffects=SpriteEffects.None;
        Rectangle size = new Rectangle();
        private float scale;
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

        public void LoadContent(ContentManager theContentManager, string theAssetName)
        {

            mSpriteTexture = theContentManager.Load<Texture2D>(theAssetName);
            scale = 0.060f;
        }
        public void Update(GameTime theGameTime)
        {
            WPosition = Wizard.Position;

            if (Math.Abs(WPosition.X - FPosition.X) >  MAX_DISTANCE)
            {
                Visible = false;
            }

            if (Visible == true)
            {
                FPosition += FDirection* FSpeed * (float)theGameTime.ElapsedGameTime.TotalSeconds;
            }
        }
        public void Draw(SpriteBatch theSpriteBatch)
        {
            if (FDirection.X == -1)
            {
                Feffects = SpriteEffects.FlipHorizontally;
            }
            else
            {
                Feffects = SpriteEffects.None;
            }
            if (Visible == true)
            {
                theSpriteBatch.Draw(mSpriteTexture, FPosition, new Rectangle(0,0,mSpriteTexture.Width,mSpriteTexture.Height), Color.White, 0.0f, Vector2.Zero, scale,Feffects, 0);

            }

        }

        public void Fire(Vector2 theStartPosition, Vector2 theSpeed,bool IsLeft)
        {

            FPosition = theStartPosition;
            FStartPosition = theStartPosition;
            FSpeed = theSpeed;
            if (IsLeft == true)
            {
                FDirection = new Vector2(-1, 0);
                FPosition.X -=Wizard.size.Width-45;
            }
            else
            {
                FDirection = new Vector2(1, 0);
            }
            Visible = true;
        }



    }

}
