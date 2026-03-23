<script setup lang="ts">
import config from "../config";
</script>

<template>
  <footer class="front-footer">
    <div class="footer-container">
      <div class="footer-content">
        <!-- 关于我们 -->
        <div class="footer-section">
          <div class="logo-section">
            <img src="../assets/logo.png" alt="Logo" class="footer-logo" />
            <h4 class="system-name">{{ config.frontName }}</h4>
          </div>
          <p class="description">
            {{ config.frontDesc }}
          </p>
        </div>
      </div>
      
      <!-- 底部版权信息 -->
      <div class="footer-bottom">
        <p class="copyright">
          © {{ config.year }} {{ config.frontName }}. All rights reserved.
        </p>
      </div>
    </div>
  </footer>
</template>

<style lang="scss" scoped>
.front-footer {
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 50%, var(--secondary-color) 100%);
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding: 60px 0 30px;
  margin-top: auto;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grain" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="25" cy="25" r="1" fill="rgba(255,255,255,0.05)"/><circle cx="75" cy="75" r="1" fill="rgba(255,255,255,0.05)"/><circle cx="50" cy="10" r="0.5" fill="rgba(255,255,255,0.03)"/><circle cx="20" cy="80" r="0.5" fill="rgba(255,255,255,0.03)"/></pattern></defs><rect width="100" height="100" fill="url(%23grain)"/></svg>');
    opacity: 0.3;
    pointer-events: none;
  }

  .footer-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
    position: relative;
    z-index: 1;
  }

  .footer-content {
    text-align: center;
    margin-bottom: 40px;

    .footer-section {
      .logo-section {
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 20px;
        transition: all 0.3s ease;
        cursor: pointer;
        padding: 12px;
        border-radius: 16px;
        display: inline-flex;

        &:hover {
          background: rgba(255, 255, 255, 0.1);
          transform: translateY(-2px);
        }

        .footer-logo {
          width: 48px;
          height: 48px;
          margin-right: 12px;
          border-radius: 50%;
          box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
          transition: all 0.3s ease;
          position: relative;

          &::after {
            content: '';
            position: absolute;
            top: -2px;
            left: -2px;
            right: -2px;
            bottom: -2px;
            background: linear-gradient(45deg, var(--primary-light), var(--secondary-color), var(--primary-color), var(--primary-lighter));
            border-radius: 50%;
            opacity: 0;
            transition: opacity 0.3s ease;
            z-index: -1;
            animation: rotate 3s linear infinite;
          }
        }

        &:hover .footer-logo::after {
          opacity: 0.7;
        }

        .system-name {
          font-size: 24px;
          font-weight: 700;
          color: white;
          margin: 0;
          text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
          position: relative;

          &::after {
            content: '';
            position: absolute;
            bottom: -4px;
            left: 0;
            height: 2px;
            width: 0;
            background: linear-gradient(90deg, var(--primary-light), var(--secondary-color));
            transition: width 0.3s ease;
          }
        }

        &:hover .system-name::after {
          width: 100%;
        }
      }

      .description {
        color: rgba(255, 255, 255, 0.9);
        font-size: 16px;
        line-height: 1.6;
        max-width: 600px;
        margin: 0 auto;
        text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
        font-weight: 400;
      }
    }
  }

  .footer-bottom {
    border-top: 1px solid rgba(255, 255, 255, 0.2);
    padding-top: 30px;
    text-align: center;
    position: relative;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 50%;
      transform: translateX(-50%);
      width: 60px;
      height: 1px;
      background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.6), transparent);
    }

    .copyright {
      color: rgba(255, 255, 255, 0.8);
      font-size: 14px;
      margin: 0;
      text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
      font-weight: 400;
    }
  }
}

@keyframes rotate {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

// 响应式设计
@media (max-width: 768px) {
  .front-footer {
    padding: 40px 0 20px;
    
    .footer-container {
      padding: 0 15px;
    }

    .footer-content {
      .footer-section {
        .logo-section {
          .footer-logo {
            width: 40px;
            height: 40px;
          }

          .system-name {
            font-size: 20px;
          }
        }

        .description {
          font-size: 14px;
        }
      }
    }
  }
}

@media (max-width: 480px) {
  .front-footer {
    padding: 30px 0 15px;

    .footer-content {
      margin-bottom: 30px;
      
      .footer-section {
        .logo-section {
          flex-direction: column;
          
          .footer-logo {
            width: 36px;
            height: 36px;
            margin-right: 0;
            margin-bottom: 8px;
          }

          .system-name {
            font-size: 18px;
          }
        }

        .description {
          font-size: 13px;
        }
      }
    }

    .footer-bottom {
      padding-top: 20px;
      
      .copyright {
        font-size: 12px;
      }
    }
  }
}
</style>