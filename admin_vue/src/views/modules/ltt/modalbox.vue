<template>
  <div v-if="isVisible" class="modal-overlay" @click.self="closeModal">
    <div class="modal">
      <div class="modal-header">
        <h3>Text File Content</h3>
        <button @click="closeModal">Close</button>
      </div>
      <div class="modal-body">
        <pre>{{ textContent }}</pre>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    isVisible: Boolean,
    textContent: String
  },
  methods: {
    closeModal() {
      this.$emit('update:isVisible', false);
    }
  }
};
</script>

<style scoped lang="scss">
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal {
  background: white;
  padding: 20px;
  border-radius: 5px;
  width: 50%;
  max-width: 500px;

  .modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    h3 {
      margin: 0;
    }

    button {
      border: none;
      background-color: #f44336;
      color: white;
      padding: 5px 10px;
      border-radius: 3px;
      cursor: pointer;

      &:hover {
        background-color: darken($color: #f44336, $amount: 10%);
      }
    }
  }

  .modal-body {
    margin-top: 20px;
    pre {
      white-space: pre-wrap;       /* CSS 3 */
      white-space: -moz-pre-wrap;  /* Mozilla, since 1999 */
      white-space: -pre-wrap;      /* Opera 4-6 */
      white-space: -o-pre-wrap;    /* Opera 7 */
      word-wrap: break-word;       /* Internet Explorer 5.5+ */
    }
  }
}
</style>
