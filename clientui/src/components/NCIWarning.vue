<script>
  export default {
    name: 'modal',
    methods: {
      close() {
        this.$emit('close');
      },
    },
  };
</script>
<template>
  <transition name="modal-fade">
    <div class="modal-backdrop">
      <div class="modal"
        role="dialog"
        aria-labelledby="modalTitle"
        aria-describedby="modalDescription">
        <header class="modal-header" id="modalTitle">
          <slot name="header">
            This is the default tile!
            <button
              type="button"
              class="btn-close"
              @click="close"
              aria-label="Close modal">x</button>
          </slot>
        </header>
        <section class="modal-body" id="modalDescription">
          <slot name="body">
            default body
          </slot>
        </section>
        <footer class="modal-footer">
          <slot name="footer">
            <button type="button" class="btn btn-primary" @click="close" aria-label="Close modal">
              Ok
            </button>
          </slot>
        </footer>
      </div>
    </div>
  </transition>
</template>
<style>
  .modal-backdrop {
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: rgba(0, 0, 0, 0.3);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .modal {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 500px;
    padding: 0px 0px 0px 15px;
    max-width: 90%;
    height: 600px;
    max-height: 90%;
    background: white;
    box-shadow: 2px 2px 20px 1px;
    overflow-x: auto;
    display: flex;
    flex-direction: column;
  }
  .modal-header,
  .modal-footer {
    padding: 10px;
    display: flex;
  }
  .modal-header {
    border-bottom: 1px solid #eeeeee;
    justify-content: space-between;
  }
  .modal-footer {
    border-top: 1px solid #eeeeee;
    justify-content: flex-end;
  }
  .modal-body {
    height: 500px;
    overflow-y: auto;
    position: relative;
    padding: 20px 10px;
  }
</style>
