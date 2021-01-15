/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 mmmmmengli@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.imengli.appletServer.daomain;


public class ThisIsLombokDemo {

    /**
     * @Data 注解在类上；提供类所有属性的 getting 和 setting 方法，此外还提供了equals、canEqual、hashCode、toString 方法
     * @Setter ：注解在属性上；为属性提供 setting 方法
     * @Setter ：注解在属性上；为属性提供 getting 方法
     * @Log4j ：注解在类上；为类提供一个 属性名为log 的 log4j 日志对象
     * @NoArgsConstructor ：注解在类上；为类提供一个无参的构造方法
     * @AllArgsConstructor ：注解在类上；为类提供一个全参的构造方法
     * @Cleanup : 可以关闭流
     * @Builder ： 被注解的类加个构造者模式
     * @Synchronized ： 加个同步锁
     * @SneakyThrows : 等同于try/catch 捕获异常
     * @NonNull : 如果给参数加个这个注解 参数为null会抛出空指针异常
     * @Value : 注解和@Data类似，区别在于它会把所有成员变量默认定义为private final修饰，并且不会生成set方法
     *
     */

}
