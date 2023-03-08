<script setup lang="ts">
import {defineProps, ref} from "vue";
import {useRouter} from "vue-router";
import axios from 'axios';

const router = useRouter();

const post = ref({
  id: 0,
  title: "",
  content: "",
});

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true,
  },
});

axios.get(`/api/posts/${props.postId}`).then((response) => {
  console.log(response)
  post.value = response.data;
});

const edit = () => {
  axios.patch(`/api/posts/${props.postId}`, post.value).then(() => {
    router.replace({name: "home"});
  });
}

</script>

<template>
  <div>
    <el-input v-model="post.title" type="text"/>
  </div>

  <div class="mt-2">
    <el-input v-model="post.content" type="textarea" rows="15"/>
  </div>

  <div class="mt-2 d-flex justify-content-end">
    <el-button type="warning" @click="edit()">수정 완료</el-button>
  </div>

</template>

<style>

</style>